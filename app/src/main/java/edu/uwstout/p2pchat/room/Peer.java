package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Class that represents the structure of the peer table.
 */
@Entity
public class Peer
{
    /**
     * MAC Address of Peer, serves as primary key
     */
    @PrimaryKey
    @ColumnInfo(name = "macAddress")
    @NotNull
    public String macAddress;
    /**
     * Nickname associated with peer
     */
    @ColumnInfo(name = "nickname")
    public String nickname;

    public Peer(String macAddress)
    {
        this.macAddress = macAddress;
    }
}
