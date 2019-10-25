package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Peer {
    @PrimaryKey
    @ColumnInfo(name = "macAddress")
    @NotNull
    public String macAddress;
    @ColumnInfo(name = "nickname")
    public String nickname;
    public Peer(String macAddress) {
        this.macAddress = macAddress;
    }
}
