package edu.uwstout.p2pchat.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Peer.class, Message.class}, version = 1)
abstract class P2pDatabase extends RoomDatabase {
    abstract public DataAccessObject dataAccessObject();
    static P2pDatabase instance;
    static P2pDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    P2pDatabase.class,
                    "p2p_database"
            ).build();
        }
        return (P2pDatabase) instance;
    }
}
