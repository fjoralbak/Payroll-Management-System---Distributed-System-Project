package com.company;

import com.company.Utils.FileHelper;
import com.company.repositories.ChatRepo;
import com.google.gson.Gson;
import models.Audio;
import models.Chat;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private int SenderID;
    private int receiverID;
    private Date koha;

    private FileHelper fileHelper;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //increase efficiency
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();
            Chat message = gson.fromJson(bufferedReader.readLine(), Chat.class);
            this.SenderID = message.getSenderID();
            this.receiverID = message.getReceiverID();

            fileHelper = FileHelper.get();
           /* int count=0;
            for(ClientHandler handler: clientHandlers){
                if(handler.SenderID!=this.SenderID){
                    count++;
                }
            }
            if(count==clientHandlers.size())*/
            clientHandlers.add(this);
            //5 0
            //5 6
            //6 0
            //6 5
            if (this.receiverID == 0) {
                for (int i = 0; i < ChatRepo.getGroupChats().size(); i++) {
                    Chat chati = ChatRepo.getGroupChats().get(i);
                    this.bufferedWriter.write(gson.toJson(chati));
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                }
                broadcastMessage(message);
            } else {
                for (int i = 0; i < ChatRepo.getCats(message.getSenderID(), message.getReceiverID()).size(); i++) {
                    Chat chati = ChatRepo.getCats(message.getSenderID(), message.getReceiverID()).get(i);
                    this.bufferedWriter.write(gson.toJson(chati));
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                }
                oneToOneMessage(message);
            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
         Chat messageFromClient;

        while (socket.isConnected()) {
            try {

                String msgFromClient = bufferedReader.readLine();
                Gson gson = new Gson();
                messageFromClient = gson.fromJson(msgFromClient, Chat.class);

                if (messageFromClient.getSendTime() == null) {
                    Audio message = gson.fromJson(msgFromClient, Audio.class);

                    if (message.getPath().contains(".mp3")) {
                        message.getChat().setMessage("audio file");
                        String filename = ("" + messageFromClient.getSenderID() + messageFromClient.getReceiverID() + messageFromClient.getSendTime() + ".mp3").replace(":", "");
                        Files.write(Path.of(fileHelper.getMediaDir() + "/" + filename), message.getFileContent());
                    }
                    else {
                        message.getChat().setMessage("video file");
                        String filename = ("" + messageFromClient.getSenderID() + messageFromClient.getReceiverID() + messageFromClient.getSendTime() + ".avi").replace(":", "");
                        Files.write(Path.of(fileHelper.getMediaDir() + "/" + filename), message.getFileContent());
                    }

                    if (this.receiverID == 0) {
                        ChatRepo.createGroupChat(message.getChat());
                        broadcastMessage(message);
                    } else {
                        ChatRepo.createChat(messageFromClient);
                        oneToOneMessage(message.getChat());
                    }

                } else {
                    System.out.println(messageFromClient.getMessage());

                    if (messageFromClient.getReceiverID() == 0) {
                        ChatRepo.createGroupChat(messageFromClient);
                        broadcastMessage(messageFromClient);
                    } else {
                        ChatRepo.createChat(messageFromClient);
                        oneToOneMessage(messageFromClient);
                    }
                }
            } catch (Exception ex) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;

            }
        }
    }

    public void broadcastMessage(Object object) {
        try {
            String message ="";
            // Date date=new Date();
            Gson gson = new Gson();
            if(object instanceof Chat){
                message = gson.toJson((Chat)object);
            }
            else{
                message = gson.toJson((Audio)object);
            }
            //if (object instanceof Chat) {
                for (ClientHandler clientHandler : clientHandlers) {

                    if (clientHandler.SenderID != this.SenderID && clientHandler.receiverID == 0 && this.receiverID == 0) {
                        clientHandler.bufferedWriter.write(message);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                }
          //  }
            /*else if (object instanceof Audio) {
                //Audio audio = (Audio) object;
                for (ClientHandler clientHandler : clientHandlers) {
                    if (clientHandler.SenderID != this.SenderID && clientHandler.receiverID == 0 && this.receiverID == 0) {
                        clientHandler.bufferedWriter.write(message);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                }*/

            //}
        } catch (IOException e) {

            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void oneToOneMessage(Object object) {
        String message ="";
        Gson gson = new Gson();
        // Date date=new Date();
        if(object instanceof Chat){
            message = gson.toJson((Chat)object);
        }
        else{
            message = gson.toJson((Audio)object);
        }
        for (ClientHandler clientHandler : clientHandlers) {
            try {

                if (clientHandler.SenderID != this.SenderID && clientHandler.receiverID == this.SenderID && clientHandler.SenderID == this.receiverID) {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        Date date = new Date();
        Chat msg = new Chat(-110, 0, date, "SERVER: " + SenderID + "has left the chat! ", "");
        broadcastMessage(msg);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

