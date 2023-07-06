package client;

import Utils.FileHelper;
import com.google.gson.Gson;
import controllers.MessengerController;
import javafx.scene.layout.VBox;
import models.Media;
import models.Chat;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class TCPClient {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int id;
    private int receiverId;
    private static int count=0;
    private String filename="filenumber:";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    private DataInputStream fileInput;
    private DataOutputStream fileOutput;
    private Date koha;
    private FileHelper fileHelper;

    public TCPClient(Socket socket, int id, int receiverId) {
        try {
            this.socket = socket;
            this.id = id;
            this.receiverId = receiverId;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.fileInput=new DataInputStream(socket.getInputStream());
            this.fileOutput=new DataOutputStream(socket.getOutputStream());
            this.fileHelper = FileHelper.get();
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String text) {
        Date date = new Date();
        try {
            // Scanner input=new Scanner(System.in);
            //hera e par qfar chat i shkon server socket
            Chat userName = new Chat(id, receiverId, date, text, "//");
            Gson gson = new Gson();
            String user = gson.toJson(userName);
            //System.out.println("jep username:");
            bufferedWriter.write(user);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            //herat tjera chat customized
           /* Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                System.out.println("jep mesazhin:");
                String messageToSend = scanner.nextLine();
                Chat msg = new Chat(id, receiverId, date, messageToSend, "//");
                String sendMessage = gson.toJson(msg);
                bufferedWriter.write(sendMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }*/
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendAudio(Media media) throws Exception{
       /* FileInputStream file=new FileInputStream(media.getPath());
        byte[]b=new byte[2002];
        file.read(b,0,b.length);
        OutputStream out =socket.getOutputStream();
        out.write(b,0,b.length);
*/
              try {
                  Gson gson=new Gson();
            // Scanner input=new Scanner(System.in);
            //hera e par qfar chat i shkon server socket
           // String path="C:Users/donat/Desktop/Servers/TCPServer/TCPserver/TcpServer/src/com/company/resources/media/"+filename+".mp3";
            //Chat userName = new Chat(id, receiverId, date, serializedFile, path);
            String user = gson.toJson(media);
            //System.out.println("jep username:");
            //bufferedWriter.write(user);


          /*  bufferedWriter.write(String.valueOf(media));
            bufferedWriter.newLine();
            bufferedWriter.flush();*/

            //herat tjera chat customized
           /* Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                System.out.println("jep mesazhin:");
                String messageToSend = scanner.nextLine();
                Chat msg = new Chat(id, receiverId, date, messageToSend, "//");
                String sendMessage = gson.toJson(msg);*/
                bufferedWriter.write(user);
                bufferedWriter.newLine();
                bufferedWriter.flush();

       } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void sendVideo(Media media) throws Exception{

        try {
            Gson gson=new Gson();
            String user = gson.toJson(media);

            bufferedWriter.write(user);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }


    //messages that have been broadcasted
    public void listenForMessage(VBox vBox) {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = bufferedReader.readLine();
                    Gson gson = new Gson();
                    Chat chat = gson.fromJson(msgFromGroupChat, Chat.class);
                    if (chat == null || chat.getSendTime() == null){
                        Media message = gson.fromJson(msgFromGroupChat, Media.class);
                        if (message.getPath().contains(".mp3")) {
                            String filename = ("" + message.getChat().getSenderID() + message.getChat().getReceiverID() + message.getChat().getSendTime() + ".mp3").replace(":", "");
                            Files.write(Path.of(fileHelper.getMediaDir() + "/" + filename), message.getFileContent());
                            System.out.println("audio tu u rujt");
                        }
                        else {
                            String filename = ("" + message.getChat().getSenderID() + message.getChat().getReceiverID() + message.getChat().getSendTime() + ".avi").replace(":", "");
                            Files.write(Path.of(fileHelper.getMediaDir() + "/" + filename), message.getFileContent());
                            System.out.println("video tu u rujt");
                        }
                        MessengerController.addLabel(message.getChat(),vBox);
                    }
                    else {
                        if (msgFromGroupChat != null) {
                            System.out.println(chat.getMessage());

                            MessengerController.addLabel(chat,vBox);
                        }

//                        MessengerController.addLabel(chat.getMessage(),vBox);
                      //  MessengerController.addLabel(chat, vBox);

                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

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

    private byte[] fileToBytes(File file) {
        byte[] bytes = null;
        File audioFile = new File(file.getPath());
        try {

            bytes = Files.readAllBytes(audioFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static void main(String[] args) throws IOException {
        /**
         Scanner scanner = new Scanner(System.in);
         System.out.println("Enter senderId:");
         int id = scanner.nextInt();
         System.out.println("Enter receiverId:");
         int receiverId = scanner.nextInt();
         //String username = scanner.nextLine();
         Socket socket = new Socket("localhost", 53000);
         TCPClient client = new TCPClient(socket, id, receiverId);

         client.listenForMessage();
         client.sendMessage();
         */
    }
}


