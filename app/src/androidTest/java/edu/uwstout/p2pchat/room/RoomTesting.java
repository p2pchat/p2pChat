package edu.uwstout.p2pchat.room;

import android.Manifest;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
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
import java.util.List;

import edu.uwstout.p2pchat.ExternalFile;
import edu.uwstout.p2pchat.InMemoryFile;

/**
 * Class used for testing that the Database is configured correctly
 */
@RunWith(AndroidJUnit4.class)
public class RoomTesting
{

    private P2pDatabase database;
    private DataAccessObject dao;

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    /**
     * Initializes an In-Memory database before running a test
     */
    @Before
    public void initDb()
    {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(), P2pDatabase.class)
                .build();
        dao = database.dataAccessObject();
    }

    /**
     * Closes the In-Memory database after finishing a test
     */
    @After
    public void closeDb()
    {
        try {
            Thread.sleep(200);

            database.close();
            database = null;
            dao = null;

            Thread.sleep(200);
        } catch(Exception e) {

        }
    }

    /**
     * Uses a LiveDataPromise to unwrap a LiveData object
     * @param liveData
     * @param <T>
     * @return
     */
    private <T> T await(LiveData<T> liveData) {
        LiveDataPromise<T> liveDataPromise = new LiveDataPromise<>(liveData);
        return liveDataPromise.await();
    }

    /**
     * Tests adding and removing peers
     */
    @Test
    public void createRetrieveUpdateDestroyPeer()
    {
        assertEquals(await(dao.getPeers()).size(), 0);

        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);
        assertEquals(await(dao.getPeers()).size(), 1);
        assertEquals(await(dao.getPeers()).get(0).macAddress, "7a:a0:83:04:03:60");
        assertEquals(await(dao.getPeers()).get(0).nickname, "Eren Jeager");

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.nickname = "Armin Arlert";
        database.dataAccessObject().insertPeers(peer2);
        assertEquals(await(dao.getPeers()).size(), 2);
        assertEquals(await(dao.getPeers()).get(1).macAddress, "7c:4f:87:2c:56:d5");
        assertEquals(await(dao.getPeers()).get(1).nickname, "Armin Arlert");

        dao.deletePeers(peer);

        assertEquals(await(dao.getPeers()).size(), 1);
        assertEquals(await(dao.getPeers()).get(0).macAddress, "7c:4f:87:2c:56:d5");
        assertEquals(await(dao.getPeers()).get(0).nickname, "Armin Arlert");

        dao.deletePeers(peer2);

        assertEquals(await(dao.getPeers()).size(), 0);

        Peer peer3 = new Peer("7b:f2:42:c2:69:10");
        peer3.nickname = "Krista Lenz";
        database.dataAccessObject().insertPeers(peer3);

        assertEquals(await(dao.getPeers()).get(0).nickname, "Krista Lenz");

        peer3.nickname = "Historia Reiss";

        database.dataAccessObject().insertPeers(peer3);

        assertEquals(await(dao.getPeers()).get(0).nickname, "Historia Reiss");

    }

    /**
     * Tests adding and removing messages
     */
    @Test(timeout = 5000)
    public void createAndDestroyMessage()
    {
        Peer peer = new Peer("7a:a0:83:04:03:60");
        peer.nickname = "Eren Jeager";
        database.dataAccessObject().insertPeers(peer);

        LiveData<List<Message>> multiChangeLiveData = dao.getMessagesFromPeer(peer.macAddress);
        LiveDataPromise<List<Message>> liveDataPromise =
        new LiveDataPromise<>(multiChangeLiveData, 3);

        Peer peer2 = new Peer("7c:4f:87:2c:56:d5");
        peer2.nickname = "Armin Arlert";
        database.dataAccessObject().insertPeers(peer2);

        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).size(), 0);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).size(), 0);

        Date now = Calendar.getInstance().getTime();

        Message message = new Message(peer.macAddress);
        message.timestamp = now;
        message.mimeType = "text/message";
        message.sent = true;
        message.content = "Historia is best girl, don't you agree Armin?";
        dao.insertMessages(message);

        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).size(), 1);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(0).content, message.content);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(0).timestamp, message.timestamp);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(0).mimeType, message.mimeType);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(0).macAddress,
                message.macAddress);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(0).sent, message.sent);

        Message message2 = new Message(peer2.macAddress);
        message2.timestamp = now;
        message2.mimeType = "text/message";
        message2.sent = false;
        message2.content = "What about Mikasa? She's head over heels for you.";
        dao.insertMessages(message2);

        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).size(), 1);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).content, message2.content);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).timestamp,
                message2.timestamp);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).mimeType, message2.mimeType);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).macAddress,
                message2.macAddress);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).sent, message2.sent);

        Message message3 = new Message(peer.macAddress);
        message3.timestamp = now;
        message3.mimeType = "text/message";
        message3.sent = true;
        message3.content = "But Historia is a literal Queen! ばか!";
        dao.insertMessages(message3);

        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).size(), 2);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(1).content, message3.content);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(1).timestamp, message3.timestamp);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(1).mimeType, message3.mimeType);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(1).macAddress,
                message3.macAddress);
        assertEquals(await(dao.getMessagesFromPeer(peer.macAddress)).get(1).sent, message3.sent);

        liveDataPromise.await(); //Testing will only pass this line if the onChange handler is
        // called at least 3 times

        // Eren is now feeling bad about that last message so he is going to delete it

        dao.deleteMessages(message3);

        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).size(), 1);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).content, message2.content);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).timestamp,
                message2.timestamp);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).mimeType, message2.mimeType);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).macAddress,
                message2.macAddress);
        assertEquals(await(dao.getMessagesFromPeer(peer2.macAddress)).get(0).sent, message2.sent);
    }

    /**
     * Tests adding and removing files from the database
     */
    @Test
    public void createAndDestroyFiles()
    {
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

        ExternalFile recoveredLyrics = await(dao.getMessagesFromPeer(peer.macAddress)).get(0).getFile();

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

        ExternalFile recoveredSnowden = await(dao.getMessagesFromPeer(peer.macAddress)).get(1).getFile();

        assertNotNull(recoveredSnowden);

        InMemoryFile loadedSnowden = recoveredSnowden.loadIntoMemory();

        assertEquals(snowden.getSize(), loadedSnowden.getSize());
        assertTrue(snowden.equals(loadedSnowden));

        savedSnowden.delete();
    }
}
