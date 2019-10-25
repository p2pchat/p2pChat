package edu.uwstout.p2pchat.room;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RoomTesting {
    Application app;
    P2pRepository repo;
    @Before
    public void setup() {
        app = new Application();
        repo = new P2pRepository(app);
    }
    @Test
    public void test_insert() {
        Peer peer = new Peer("makjdflkasjkldfj");
        peer.nickname = "test";
        repo.insertPeers(peer);
        Thread.sleep(100);
        Peer[] peers = repo.getPeers();
        assertThat(peers.length).isEqualTo(1);
        assertThat(peers[0].nickname).isEqualTo("test");
    }
    @Test
    public void verify_uuid_unique()
    {
        UUID[] uuids = new UUID[500];
        for(int i = 0; i < 500; i++) {
            uuids[i] = UUID.randomUUID();
        }
        HashSet<UUID> isUnique = new HashSet<UUID>(Arrays.asList(uuids));
        assertThat(uuids.length).isEqualTo(isUnique.size());
    }
}