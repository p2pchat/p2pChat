package edu.uwstout.p2pchat;

public class P2PMessage
{
    private String text;
    private String timestamp;
    private String status;
    private String messageType;

    /**
     *
     * @param text
     * @param timestamp
     * @param status
     * @param messageType
     */
    public P2PMessage(String text, String timestamp, String status, String messageType)
    {
        this.text = text;
        this.timestamp = timestamp;
        this.status = status;
        this.messageType = messageType;
    }

    /**
     *
     * @return
     */
    String getText()
    {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     *
     * @return
     */
    String getTimestamp()
    {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    String getStatus()
    {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     *
     * @return
     */
    String getMessageType()
    {
        return messageType;
    }

    /**
     *
     * @param messageType
     */
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }
}
