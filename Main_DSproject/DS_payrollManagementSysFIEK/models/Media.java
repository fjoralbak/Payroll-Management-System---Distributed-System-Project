package models;

import java.io.File;

public class Media {
    private Chat chat;
    private byte[] file;

    public Media(Chat chat, byte[] fileContent) {
        this.chat = chat;
        this.file = fileContent;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public byte[] getFileContent() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    public String getPath(){
        return this.chat.getPath();
    }
}
