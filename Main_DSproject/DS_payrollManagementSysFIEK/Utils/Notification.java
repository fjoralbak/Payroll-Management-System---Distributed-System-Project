package Utils;

import javafx.scene.paint.Color;

public class Notification {
    private  String msg;
    private NotificaitonType type;

    public Notification(String msg,NotificaitonType type)
    {
        this.msg=msg;
        this.type=type;
    }

    public Color getMsgColor()
    {
        switch(this.type)
        {
            case INFO:
                return Color.web("0x5bc0de");
            case WARNING:
                return Color.web("0xf0ad4e");
            case SUCC:
                return Color.web("0x5cb85c");
            case ERROR:
                return Color.web("0xd9534f");
            default:
                return Color.web("0x0275d8");
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NotificaitonType getType() {
        return type;
    }

    public void setType(NotificaitonType type) {
        this.type = type;
    }
}