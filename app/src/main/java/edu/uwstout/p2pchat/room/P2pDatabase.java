package edu.uwstout.p2pchat.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Database holding the peer and message tables.
 */
@Database(entities = {Peer.class, Message.class}, version = 1)
@TypeConverters(Converters.class)
abstract class P2pDatabase extends RoomDatabase
{
    /**
     * @return instance of DataAccessObject to be used in CRUD operations
     */
    public abstract DataAccessObject dataAccessObject();

    /**
     * Application wide single instance of this Database.
     */
    private static P2pDatabase instance;

    /**
     * Returns or creates instance of the database using the Singleton design pattern.
     * @param context Application context
     * @return Instance of P2pDatabase
     */
    static P2pDatabase getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(
                    context,
                    P2pDatabase.class,
                    "p2p_database"
            ).build();
        }
        return (P2pDatabase) instance;
    }
}
