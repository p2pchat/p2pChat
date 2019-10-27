package edu.uwstout.p2pchat.room;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataAccessObject {
    @Insert
    void insertPeers(Peer... peers);
    @Insert
    void insertMessages(Message... messages);
    @Delete
    void deletePeers(Peer... peers);
    @Delete
    void deleteMessages(Message... messages);
    @Query("SELECT * FROM message WHERE macAddress = :macAddress")
    LiveData<List<Message>> getMessagesFromPeer(String macAddress);
    @Query("SELECT * FROM peer")
    LiveData<List<Peer>> getPeers();
}
