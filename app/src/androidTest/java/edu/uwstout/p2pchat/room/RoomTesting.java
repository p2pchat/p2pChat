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

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
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
    public void createAndDestroyPeer() {
        DataAccessObject dao = database.dataAccessObject();
        assertEquals(dao.getPeers().size(), 0);

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);
        assertEquals(dao.getPeers().size(), 1);
        assertEquals(dao.getPeers().get(0).macAddress, "7a:a0:83:04:03:60");
        assertEquals(dao.getPeers().get(0).nickname, "Eren Jeager");

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.nickname = "Armin Arlert";
        database.dataAccessObject().insertPeers(peer2);
        assertEquals(dao.getPeers().size(), 2);
        assertEquals(dao.getPeers().get(1).macAddress, "7c:4f:87:2c:56:d5");
        assertEquals(dao.getPeers().get(1).nickname, "Armin Arlert");

        dao.deletePeers(peer);

        assertEquals(dao.getPeers().size(), 1);
        assertEquals(dao.getPeers().get(0).macAddress, "7c:4f:87:2c:56:d5");
        assertEquals(dao.getPeers().get(0).nickname, "Armin Arlert");

        dao.deletePeers(peer2);

        assertEquals(dao.getPeers().size(), 0);
    }

    @Test
    public void createAndDestroyMessage() {
        DataAccessObject dao = database.dataAccessObject();

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.nickname = "Armin Arlert";
        database.dataAccessObject().insertPeers(peer2);

        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").size(), 0);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").size(), 0);

        Date now = Calendar.getInstance().getTime();

        Message message = new Message();
        message.timestamp = now;
        message.mimeType = "text/plain";
        message.macAddress = "7a:a0:83:04:03:60";
        message.sent = true;
        message.content = "Historia is best girl, don't you agree Armin?";
        dao.insertMessages(message);

        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").size(), 1);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(0).content, message.content);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(0).timestamp, message.timestamp);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(0).mimeType, message.mimeType);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(0).macAddress, message.macAddress);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(0).sent, message.sent);

        Message message2 = new Message();
        message2.timestamp = now;
        message2.mimeType = "text/plain";
        message2.macAddress = "7c:4f:87:2c:56:d5";
        message2.sent = false;
        message2.content = "What about Mikasa? She's head over heels for you.";
        dao.insertMessages(message2);

        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").size(), 1);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).content, message2.content);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).timestamp, message2.timestamp);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).mimeType, message2.mimeType);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).macAddress, message2.macAddress);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).sent, message2.sent);

        Message message3 = new Message();
        message3.timestamp = now;
        message3.mimeType = "text/plain";
        message3.macAddress = "7a:a0:83:04:03:60";
        message3.sent = true;
        message3.content = "But Historia is a literal Queen! ばか!";
        dao.insertMessages(message3);

        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").size(), 2);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(1).content, message3.content);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(1).timestamp, message3.timestamp);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(1).mimeType, message3.mimeType);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(1).macAddress, message3.macAddress);
        assertEquals(dao.getMessagesFromPeer("7a:a0:83:04:03:60").get(1).sent, message3.sent);

        // Eren is now feeling bad about that last message so he is going to delete it

        dao.deleteMessages(message3);

        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").size(), 1);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).content, message2.content);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).timestamp, message2.timestamp);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).mimeType, message2.mimeType);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).macAddress, message2.macAddress);
        assertEquals(dao.getMessagesFromPeer("7c:4f:87:2c:56:d5").get(0).sent, message2.sent);
    }
}
