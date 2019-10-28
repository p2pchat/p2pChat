package edu.uwstout.p2pchat.room;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

/**
 * Interface for CRUD operations on the database.
 */
@Dao
public interface DataAccessObject
{
    /**
     * Inserts new Peer values into the peer table.
     * @param peers values to be inserted
     */
    @Insert
    void insertPeers(Peer... peers);

    /**
     * Inserts new Messages into the message table.
     * @param messages values to be inserted
     */
    @Insert
    void insertMessages(Message... messages);

    /**
     * Deletes peers from the peer table.
     * @param peers values to be deleted
     */
    @Delete
    void deletePeers(Peer... peers);

    /**
     * Deletes messages from the message table.
     * @param messages values to be deleted.
     */
    @Delete
    void deleteMessages(Message... messages);

    /**
     * Returns all messages referencing a particular MAC Address.
     * @param macAddress MAC Address to filter by
     * @return List of Messages associated with a MAC Address.
     */
    @Query("SELECT * FROM message WHERE macAddress = :macAddress")
    List<Message> getMessagesFromPeer(String macAddress);

    /**
     * Returns a list of all peers in the database.
     * @return List of Peers in database.
     */
    @Query("SELECT * FROM peer")
    List<Peer> getPeers();
}
