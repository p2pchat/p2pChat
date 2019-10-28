package edu.uwstout.p2pchat.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    private String macAddress;
    /**
     * Nickname associated with peer
     */
    @ColumnInfo(name = "nickname")
    private String nickname;

    /**
     * @return MAC Address of peer
     */
    @NotNull
    public final String getMacAddress()
    {
        return macAddress;
    }

    /**
     * @return Nickname of peer
     */
    public final String getNickname()
    {
        return nickname;
    }

    /**
     * @param nicknameStr Nickname setter
     */
    public final void setNickname(final String nicknameStr)
    {
        nickname = nicknameStr;
    }

    /**
     * Peer constructor
     * @param macAddressStr MAC Address of peer
     */
    public Peer(@NotNull final String macAddressStr)
    {
        macAddress = macAddressStr;
    }
}
