package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.File;
import java.util.Date;

import edu.uwstout.p2pchat.ExternalFile;

/**
 * Represents the structure of the message table.
 */
@Entity(foreignKeys = @ForeignKey(entity = Peer.class, parentColumns = "macAddress",
        childColumns = "macAddress", onDelete = ForeignKey.CASCADE))
public class Message
{

    /**
     * Primary key, auto generated
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    /**
     * Time message was sent/received
     */
    @ColumnInfo(name = "timestamp")
    private Date timestamp;
    /**
     * Foreign key of Peer message with sent to or received from
     */
    @ColumnInfo(name = "macAddress")
    private String macAddress;
    /**
     * Determines if message was sent or received.
     */
    @ColumnInfo(name = "sent")
    private Boolean sent;
    /**
     * Mime type of message. Will be equal to "text/message" for standard inline text messages.
     */
    @ColumnInfo(name = "mimeType")
    private String mimeType;
    /**
     * Content of message. Could either be a plaintext string or path to file.
     */
    @ColumnInfo(name = "content")
    private String content;

    /**
     * @return ID of message
     */
    public final int getId()
    {
        return id;
    }

    /**
     * @return Timestamp of message
     */
    public final Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * @param timestampRef Value to set timestamp to
     */
    public final void setTimestamp(final Date timestampRef)
    {
        timestamp = timestampRef;
    }

    /**
     * @return MAC Address of message
     */
    public final String getMacAddress()
    {
        return macAddress;
    }

    /**
     * @param macAddressStr MAC Address setter
     */
    public final void setMacAddress(final String macAddressStr)
    {
        macAddress = macAddressStr;
    }

    /**
     * @return Sent value of message
     */
    public final Boolean getSent()
    {
        return sent;
    }

    /**
     * @param sentVal Sent setter
     */
    public final void setSent(final Boolean sentVal)
    {
        sent = sentVal;
    }

    /**
     * @return Gets Message Mime type
     */
    public final String getMimeType()
    {
        return mimeType;
    }

    /**
     * @param mimeTypeStr Mime type setter
     */
    public final void setMimeType(final String mimeTypeStr)
    {
        this.mimeType = mimeTypeStr;
    }

    /**
     * @return Message content getter
     */
    public final String getContent()
    {
        return content;
    }

    /**
     * @param contentStr Message content setter
     */
    public final void setContent(final String contentStr)
    {
        content = contentStr;
    }

    /**
     * Message constructor.
     * @param macAddressStr MAC Address of Peer this message was sent to or received from.
     */
    public Message(final String macAddressStr)
    {
        macAddress = macAddressStr;
    }

    /**
     * Returns true if this message is containing a file.
     * @return if message is referencing an external file.
     */
    public final Boolean isFile()
    {
        return mimeType != "text/message";
    }

    /**
     * Returns a reference to this message's file if exists. Null otherwise.
     * @return ExternalFile reference to message's file. Otherwise null.
     */
    public final ExternalFile getFile()
    {
        if (isFile())
        {
            return new ExternalFile(new File(content), mimeType);
        }
        return null;
    }
}
