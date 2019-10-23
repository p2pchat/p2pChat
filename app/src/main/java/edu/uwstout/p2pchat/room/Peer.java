package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Peer {
    @PrimaryKey
    public String macAddress;
    @ColumnInfo(name = "nickname")
    public String nickname;

}
