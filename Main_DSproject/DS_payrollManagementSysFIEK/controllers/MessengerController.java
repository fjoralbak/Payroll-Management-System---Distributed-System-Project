package controllers;

import Utils.FileHelper;
import Utils.SessionManager;
import client.TCPClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Media;

import models.Chat;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.ResourceBundle;

public class MessengerController extends ChildController {

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private RadioButton startVoiceRec;
    @FXML
    private RadioButton stopVoiceRec;
    @FXML
    private RadioButton startVideoRec;
    @FXML
    private RadioButton stopVideoRec;

    private static final int CAMERA_ID = 0;
    private static VideoCapture videoCapture;
    Size size;
    Mat matrix;
    private static VideoWriter videoWriter;

    private TCPClient client;
    private TargetDataLine target;

    private static File output = null;
    private FileHelper fileHelper;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fileHelper = FileHelper.get();
            // client = new TCPClient(new Socket("localhost", 5678), parseInt(senderID.getText()), parseInt(receiverID.getText()));
            if (SessionManager.staffSelected == null) {
                client = new TCPClient(new Socket("localhost", 5678), SessionManager.user.getSSN(), 0);
            } else {
                client = new TCPClient(new Socket("localhost", 5678), SessionManager.user.getSSN(), SessionManager.staffSelected.getSSN());
                SessionManager.staffSelected = null;
            }
            System.out.println("Connected to server");
            AudioFormat audio = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info datainfo = new DataLine.Info(TargetDataLine.class, audio);
            if (!AudioSystem.isLineSupported(datainfo)) {
                System.out.println("not supported");
            }
            target = (TargetDataLine) AudioSystem.getLine(datainfo);
            target.open();

            // load the native OpenCV library
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            //		launch(args);
            //		System.load( "C:\\opencv\\build\\java\\x64\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
            System.load( "C:\\opencv\\build\\bin\\opencv_ffmpeg3415_64.dll");
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        vbox_message.heightProperty().addListener((observableValue, number, t1) -> sp_main.setVvalue((Double) t1));

        client.sendMessage("user with name:" + SessionManager.user.getName() + " connected");

        client.listenForMessage(vbox_message);

        /*button_send.setOnAction(actionEvent -> {
            String messageToSend = tf_message.getText();
            if (!messageToSend.isEmpty()) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));

                Text text = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(text);

                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(15,125,242);"
                        + "-fx-background-radius: 20px;");

                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(0.934, 0.945, 0.996));

                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);

                client.sendMessage(text);
                tf_message.clear();
            }
        })*/
        ;
    }
    public String pathForFile(){
        output=new File("");
        return output.getName();
    }

    public static void addLabel(Chat msgFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(msgFromServer.getMessage());

        if(msgFromServer.getMessage().equals("audio file")){
            Button button = new Button("play audio");String filename = ("" + msgFromServer.getSenderID() + msgFromServer.getReceiverID() + msgFromServer.getSendTime() + ".mp3").replace(":", "");
            button.setId(filename);
            button.setOnAction(actionEvent -> playSound((FileHelper.get().getMediaDir() + "/" + filename)));
            if (msgFromServer.getSenderID() == SessionManager.user.getSSN()) {
                button.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(15,125,242);"
                        + "-fx-background-radius: 20px;");
            } else {
                button.setStyle("-fx-background-color: rgb(233,233,235);"
                        + "-fx-background-radius: 20px;");
            }
//            button.setId(msgFromServer.getPath());
            button.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(button);
        }
        else if(msgFromServer.getMessage().equals("video file")){
            Button button = new Button("play video");
            String filename = ("" + msgFromServer.getSenderID() + msgFromServer.getReceiverID() + msgFromServer.getSendTime() + ".avi").replace(":", "");
            button.setId(filename);
            button.setOnAction(actionEvent -> playVideo((FileHelper.get().getMediaDir() + "/" + filename)));
            if (msgFromServer.getSenderID() == SessionManager.user.getSSN()) {
                button.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(15,125,242);"
                        + "-fx-background-radius: 20px;");
            } else {
                button.setStyle("-fx-background-color: rgb(233,233,235);"
                        + "-fx-background-radius: 20px;");
            }
//            button.setId(msgFromServer.getPath());
            button.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(button);
        }
        else{
            TextFlow textFlow = new TextFlow(text);
            if (msgFromServer.getSenderID() == SessionManager.user.getSSN()) {
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(15,125,242);"
                        + "-fx-background-radius: 20px;");
            } else {
                textFlow.setStyle("-fx-background-color: rgb(233,233,235);"
                        + "-fx-background-radius: 20px;");
            }
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);
        }

        //text.setFill(Color.color(0.934,0.945,0.996));

        //vBox.getChildren().add(hBox);
        Platform.runLater(() -> vBox.getChildren().add(hBox));

    }

    @FXML
    public void startRec() {
        this.stopVoiceRec.setSelected(false);
        target.start();
        String filename = ("" + this.client.getId() + this.client.getReceiverId() + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) + ".mp3").replace(":", "");
        final File outPutFile = new File((fileHelper.getMediaDir() + "/" + filename));
        Thread audioRecorder = new Thread(() -> {
            AudioInputStream recordingStream = new AudioInputStream(target);
            try {
                AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outPutFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Stopped recording");
        });
        audioRecorder.start();
        output = outPutFile;
    }

    @FXML
    public void stopRec() throws Exception{
        Date date = new Date();
        this.startVoiceRec.setSelected(false);
        target.stop();
        target.close();
        Media media;
        if (SessionManager.staffSelected != null) {
            media = new Media(new Chat(SessionManager.user.getSSN(), SessionManager.staffSelected.getSSN(), date, "", ""),fileToBytes(output));
        } else {
            media = new Media(new Chat(SessionManager.user.getSSN(), 0, date, "", ""), fileToBytes(output));
        }
        client.sendAudio(media);
    }

    @FXML
    public void startVideoRec() {
        this.stopVideoRec.setSelected(false);
        Thread videoRecorder = new Thread(() -> {
            videoCapture = new VideoCapture(0);

            if (videoCapture.isOpened()) {
                Mat m = new Mat();
                videoCapture.read(m);

                int fourcc = VideoWriter.fourcc('M','J','P','G');

                double fps = 25;
                Size s =  new Size((int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
                String filename = ("" + this.client.getId() + this.client.getReceiverId() + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) + ".avi").replace(":", "");
                output = new File(fileHelper.getMediaDir() + "/" + filename);
                videoWriter = new VideoWriter(fileHelper.getMediaDir() + "/" + filename, fourcc, fps, s, true);

                while (videoCapture.read(m)) {
                    videoWriter.write(m);
                    HighGui.imshow("Frame", m);
                    int key = HighGui.waitKey(20);
                    if (key == 81)
                    {
                        System.out.println("Stopping the video");
                        break;
                    }
                }
            }
            try {
                this.stopVideoRec();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        videoRecorder.start();
    }

    @FXML
    public void stopVideoRec() throws Exception{
        this.startVideoRec.setSelected(false);
        System.out.println("Stopping the video");
        videoCapture.release();
        videoWriter.release();
        HighGui.destroyAllWindows();
        Date date = new Date();
        Media media;
        String filename = ("" + this.client.getId() + this.client.getReceiverId() + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()) + ".mp3").replace(":", "");

        if (SessionManager.staffSelected != null) {
            media = new Media(new Chat(SessionManager.user.getSSN(), SessionManager.staffSelected.getSSN(), date, "", filename),fileToBytes(output));
        } else {
            media = new Media(new Chat(SessionManager.user.getSSN(), 0, date, "", filename), fileToBytes(output));
        }
        client.sendVideo(media);
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

    private static void playSound(String path) {
        try {
            //  Media media = new Media(output.toURI().toString());
            // System.out.println();
            // MediaPlayer player = new MediaPlayer(media);
            // player.setCycleCount(MediaPlayer.INDEFINITE);
            //player.play();
            File file = new File(path);
            AudioInputStream input = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(input);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void playVideo(String path) {
        videoCapture = new VideoCapture(path);

        if (!videoCapture.isOpened()) {
            System.out.println("Error opening the video file");
        } else {
            int fps = (int) videoCapture.get(5);
            double frame_count = videoCapture.get(7);
            while (videoCapture.isOpened()) {
                Mat m = new Mat();
                boolean isSuccess = videoCapture.read(m);
                if(isSuccess == true) {
                    HighGui.imshow("Frame", m);
                }
                else {
                    System.out.println("Video camera is disconnected");
                    break;
                }

                int key = HighGui.waitKey(20);
                if (key == 81)
                {
                    System.out.println("Stopping the video");
                    break;
                }
            }
        }

        videoCapture.release();
        HighGui.destroyAllWindows();
    }

    @FXML
    private void onSaveBtnClick(ActionEvent event) throws Exception {
        try {
            String messageToSend = tf_message.getText();
            if (!messageToSend.isEmpty()) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));

                Text text = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(text);

                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(15,125,242);"
                        + "-fx-background-radius: 20px;");

                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);

                client.sendMessage(text.getText());
                tf_message.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
