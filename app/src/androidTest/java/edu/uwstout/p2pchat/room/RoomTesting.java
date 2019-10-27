package edu.uwstout.p2pchat.room;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RoomTesting {

    private P2pDatabase database;

    @Before
    public void initDb() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), P2pDatabase.class).build();
    }
    @After
    public void closeDb() {
        database.close();
        database = null;
    }

    @Test
    public void insertPeer() {
        DataAccessObject dao = database.dataAccessObject();
        LiveData<List<Peer>> peers = dao.getPeers();
        assertEquals(peers.getValue().size(), 0);
        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);
        assertEquals(peers.getValue().size(), 1);
        assertEquals(peers.getValue().get(0).macAddress, "7a:a0:83:04:03:60");
        assertEquals(peers.getValue().get(0).nickname, "Eren Jeager");
    }
}
