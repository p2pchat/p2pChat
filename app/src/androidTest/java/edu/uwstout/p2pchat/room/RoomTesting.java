package edu.uwstout.p2pchat.room;

import android.Manifest;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import edu.uwstout.p2pchat.ExternalFile;
import edu.uwstout.p2pchat.InMemoryFile;

@RunWith(AndroidJUnit4.class)
public class RoomTesting
{

    private P2pDatabase database;

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void initDb()
    {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(), P2pDatabase.class)
                .build();
    }

    @After
    public void closeDb()
    {
        database.close();
        database = null;
    }

    @Test
    public void createRetrieveUpdateDestroyPeer()
    {
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

        Peer peer3 = new Peer("7b:f2:42:c2:69:10");
        peer3.nickname = "Krista Lenz";
        database.dataAccessObject().insertPeers(peer3);

        assertEquals(dao.getPeers().get(0).nickname, "Krista Lenz");

        database.dataAccessObject().updatePeerNickname(peer3.macAddress, "Historia Reiss");

        assertEquals(dao.getPeers().get(0).nickname, "Historia Reiss");

    }

    @Test
    public void createAndDestroyMessage()
    {
        DataAccessObject dao = database.dataAccessObject();

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.nickname = "Armin Arlert";
        database.dataAccessObject().insertPeers(peer2);

        assertEquals(dao.getMessagesFromPeer(peer.macAddress).size(), 0);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).size(), 0);

        Date now = Calendar.getInstance().getTime();

        Message message = new Message(peer.macAddress);
        message.timestamp = now;
        message.mimeType = "text/message";
        message.sent = true;
        message.content = "Historia is best girl, don't you agree Armin?";
        dao.insertMessages(message);

        assertEquals(dao.getMessagesFromPeer(peer.macAddress).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(0).content, message.content);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(0).timestamp, message.timestamp);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(0).mimeType, message.mimeType);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(0).macAddress,
                message.macAddress);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(0).sent, message.sent);

        Message message2 = new Message(peer2.macAddress);
        message2.timestamp = now;
        message2.mimeType = "text/message";
        message2.sent = false;
        message2.content = "What about Mikasa? She's head over heels for you.";
        dao.insertMessages(message2);

        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).content, message2.content);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).timestamp,
                message2.timestamp);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).mimeType, message2.mimeType);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).macAddress,
                message2.macAddress);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).sent, message2.sent);

        Message message3 = new Message(peer.macAddress);
        message3.timestamp = now;
        message3.mimeType = "text/message";
        message3.sent = true;
        message3.content = "But Historia is a literal Queen! ばか!";
        dao.insertMessages(message3);

        assertEquals(dao.getMessagesFromPeer(peer.macAddress).size(), 2);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(1).content, message3.content);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(1).timestamp, message3.timestamp);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(1).mimeType, message3.mimeType);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(1).macAddress,
                message3.macAddress);
        assertEquals(dao.getMessagesFromPeer(peer.macAddress).get(1).sent, message3.sent);

        // Eren is now feeling bad about that last message so he is going to delete it

        dao.deleteMessages(message3);

        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).content, message2.content);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).timestamp,
                message2.timestamp);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).mimeType, message2.mimeType);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).macAddress,
                message2.macAddress);
        assertEquals(dao.getMessagesFromPeer(peer2.macAddress).get(0).sent, message2.sent);
    }

    @Test
    public void createAndDestroyFiles()
    {
        DataAccessObject dao = database.dataAccessObject();

        InMemoryFile lyrics = null;
        InMemoryFile snowden = null;

        try
        {
            lyrics = new InMemoryFile("lyrics.txt",
                    InstrumentationRegistry.getInstrumentation().getContext().getResources()
                            .getAssets().open("lyrics.txt"), "text/plain");
            snowden = new InMemoryFile("snowden.png",
                    InstrumentationRegistry.getInstrumentation().getContext().getResources()
                            .getAssets().open("snowden.png"), "image/png");
        }
        catch (Exception e)
        {
            fail();
        }

        assertNotEquals(lyrics.getSize(), 0);
        assertNotEquals(snowden.getSize(), 0);

        Date now = Calendar.getInstance().getTime();

        ExternalFile savedLyrics = lyrics.saveToStorage(
                InstrumentationRegistry.getInstrumentation().getContext(), now);
        ExternalFile savedSnowden = snowden.saveToStorage(
                InstrumentationRegistry.getInstrumentation().getContext(), now);

        assertNotNull(savedLyrics);
        assertNotNull(savedSnowden);

        assertTrue(savedLyrics.exists());
        assertTrue(savedSnowden.exists());

        Peer peer = new Peer("8a:c4:03:c2:73:87");
        peer.nickname = "Tony Stark";

        dao.insertPeers(peer);

        Message messageLyrics = new Message(peer.macAddress);
        messageLyrics.sent = false;
        messageLyrics.mimeType = "text/plain";
        messageLyrics.timestamp = now;
        messageLyrics.content = savedLyrics.getPath();

        dao.insertMessages(messageLyrics);

        ExternalFile recoveredLyrics = dao.getMessagesFromPeer(peer.macAddress).get(0).getFile();

        assertNotNull(recoveredLyrics);

        InMemoryFile loadedLyrics = recoveredLyrics.loadIntoMemory();

        assertEquals(lyrics.getSize(), loadedLyrics.getSize());
        assertTrue(lyrics.equals(loadedLyrics));

        savedLyrics.delete();

        Message messageSnowden = new Message(peer.macAddress);
        messageSnowden.sent = false;
        messageSnowden.mimeType = "image/png";
        messageSnowden.timestamp = now;
        messageSnowden.content = savedSnowden.getPath();

        dao.insertMessages(messageSnowden);

        ExternalFile recoveredSnowden = dao.getMessagesFromPeer(peer.macAddress).get(1).getFile();

        assertNotNull(recoveredSnowden);

        InMemoryFile loadedSnowden = recoveredSnowden.loadIntoMemory();

        assertEquals(snowden.getSize(), loadedSnowden.getSize());
        assertTrue(snowden.equals(loadedSnowden));

        savedSnowden.delete();
    }
}
