package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Peer.class, parentColumns = "macAddress", childColumns = "macAddress", onDelete = ForeignKey.CASCADE))
public class Message {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "timestamp")
    public Date timestamp;
    @ColumnInfo(name = "macAddress")
    public String macAddress;
    @ColumnInfo(name = "sent")
    public Boolean sent;
    @ColumnInfo(name = "mimeType")
    public String mimeType;
    @ColumnInfo(name = "content")
    public String content;
}
