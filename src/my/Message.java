package my;

public class Message {

    private String messageid;
    private String time;
    private String sender;
    private String receiver;
    private String text;
    private String read;

    public String getMessageid() {
        return messageid;
    }
    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getRead() {
        return read;
    }
    public void setRead(String read) {
        this.read = read;
    }


}
