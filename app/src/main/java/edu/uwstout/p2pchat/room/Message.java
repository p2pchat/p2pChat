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
    public int id;
    /**
     * Time message was sent/received
     */
    @ColumnInfo(name = "timestamp")
    public Date timestamp;
    /**
     * Foreign key of Peer message with sent to or received from
     */
    @ColumnInfo(name = "macAddress")
    public String macAddress;
    /**
     * Determines if message was sent or received.
     */
    @ColumnInfo(name = "sent")
    public Boolean sent;
    /**
     * Mime type of message. Will be equal to "text/message" for standard inline text messages.
     */
    @ColumnInfo(name = "mimeType")
    public String mimeType;
    /**
     * Content of message. Could either be a plaintext string or path to file.
     */
    @ColumnInfo(name = "content")
    public String content;

    public Message(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public Boolean isFile()
    {
        return mimeType != "text/message";
    }

    /**
     * Returns a reference to this message's file if exists. Null otherwise.
     * @return ExternalFile reference to message's file. Otherwise null.
     */
    public ExternalFile getFile()
    {
        if (isFile())
        {
            return new ExternalFile(new File(content), mimeType);
        }
        return null;
    }
}
