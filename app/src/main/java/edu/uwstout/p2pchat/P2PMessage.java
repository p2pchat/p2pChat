package edu.uwstout.p2pchat;

public class P2PMessage
{
    private String text;
    private String timestamp;
    private String status;
    private String messageType;
    public P2PMessage(String text, String timestamp, String status, String messageType)
    {
        this.text = text;
        this.timestamp = timestamp;
        this.status = status;
        this.messageType = messageType;
    }


    String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }
}
