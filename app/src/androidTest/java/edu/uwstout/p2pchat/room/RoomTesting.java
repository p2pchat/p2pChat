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
    public void createAndDestroyPeer()
    {
        DataAccessObject dao = database.dataAccessObject();
        assertEquals(dao.getPeers().size(), 0);

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.setNickname("Eren Jeager");
        database.dataAccessObject().insertPeers(peer);
        assertEquals(dao.getPeers().size(), 1);
        assertEquals(dao.getPeers().get(0).getMacAddress(), "7a:a0:83:04:03:60");
        assertEquals(dao.getPeers().get(0).getNickname(), "Eren Jeager");

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.setNickname("Armin Arlert");
        database.dataAccessObject().insertPeers(peer2);
        assertEquals(dao.getPeers().size(), 2);
        assertEquals(dao.getPeers().get(1).getMacAddress(), "7c:4f:87:2c:56:d5");
        assertEquals(dao.getPeers().get(1).getNickname(), "Armin Arlert");

        dao.deletePeers(peer);

        assertEquals(dao.getPeers().size(), 1);
        assertEquals(dao.getPeers().get(0).getMacAddress(), "7c:4f:87:2c:56:d5");
        assertEquals(dao.getPeers().get(0).getNickname(), "Armin Arlert");

        dao.deletePeers(peer2);

        assertEquals(dao.getPeers().size(), 0);
    }

    @Test
    public void createAndDestroyMessage()
    {
        DataAccessObject dao = database.dataAccessObject();

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.setNickname("Eren Jeager");
        database.dataAccessObject().insertPeers(peer);

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.setNickname("Armin Arlert");
        database.dataAccessObject().insertPeers(peer2);

        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).size(), 0);
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).size(), 0);

        Date now = Calendar.getInstance().getTime();

        Message message = new Message(peer.getMacAddress());
        message.setTimestamp(now);
        message.setMimeType("text/message");
        message.setSent(true);
        message.setContent("Historia is best girl, don't you agree Armin?");
        dao.insertMessages(message);

        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getContent(), message.getContent());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getTimestamp(), message.getTimestamp());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getMimeType(), message.getMimeType());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getMacAddress(),
                message.getMacAddress());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getSent(), message.getSent());

        Message message2 = new Message(peer2.getMacAddress());
        message2.setTimestamp(now);
        message2.setMimeType("text/message");
        message2.setSent(false);
        message2.setContent("What about Mikasa? She's head over heels for you.");
        dao.insertMessages(message2);

        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getContent(), message2.getContent());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getTimestamp(),
                message2.getTimestamp());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getMimeType(), message2.getMimeType());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getMacAddress(),
                message2.getMacAddress());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getSent(), message2.getSent());

        Message message3 = new Message(peer.getMacAddress());
        message3.setTimestamp(now);
        message3.setMimeType("text/message");
        message3.setSent(true);
        message3.setContent("But Historia is a literal Queen! ばか!");
        dao.insertMessages(message3);

        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).size(), 2);
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getContent(), message3.getContent());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getTimestamp(), message3.getTimestamp());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getMimeType(), message3.getMimeType());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getMacAddress(),
                message3.getMacAddress());
        assertEquals(dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getSent(), message3.getSent());

        // Eren is now feeling bad about that last message so he is going to delete it

        dao.deleteMessages(message3);

        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).size(), 1);
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getContent(), message2.getContent());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getTimestamp(),
                message2.getTimestamp());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getMimeType(), message2.getMimeType());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getMacAddress(),
                message2.getMacAddress());
        assertEquals(dao.getMessagesFromPeer(peer2.getMacAddress()).get(0).getSent(), message2.getSent());
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
        peer.setNickname("Tony Stark");

        dao.insertPeers(peer);

        Message messageLyrics = new Message(peer.getMacAddress());
        messageLyrics.setSent(false);
        messageLyrics.setMimeType("text/plain");
        messageLyrics.setTimestamp(now);
        messageLyrics.setContent(savedLyrics.getPath());

        dao.insertMessages(messageLyrics);

        ExternalFile recoveredLyrics = dao.getMessagesFromPeer(peer.getMacAddress()).get(0).getFile();

        assertNotNull(recoveredLyrics);

        InMemoryFile loadedLyrics = recoveredLyrics.loadIntoMemory();

        assertEquals(lyrics.getSize(), loadedLyrics.getSize());
        assertTrue(lyrics.equals(loadedLyrics));

        savedLyrics.delete();

        Message messageSnowden = new Message(peer.getMacAddress());
        messageSnowden.setSent(false);
        messageSnowden.setMimeType("image/png");
        messageSnowden.setTimestamp(now);
        messageSnowden.setContent(savedSnowden.getPath());

        dao.insertMessages(messageSnowden);

        ExternalFile recoveredSnowden = dao.getMessagesFromPeer(peer.getMacAddress()).get(1).getFile();

        assertNotNull(recoveredSnowden);

        InMemoryFile loadedSnowden = recoveredSnowden.loadIntoMemory();

        assertEquals(snowden.getSize(), loadedSnowden.getSize());
        assertTrue(snowden.equals(loadedSnowden));

        savedSnowden.delete();
    }
}
