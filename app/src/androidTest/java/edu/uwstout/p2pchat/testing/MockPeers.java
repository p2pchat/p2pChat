package edu.uwstout.p2pchat.testing;

import edu.uwstout.p2pchat.room.Peer;

public class MockPeers {
    private static Peer makePeer(String macAddress, String nickname) {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        return peer;
    }
    public static final Peer austin = makePeer("d0:6e:8c:dd:86:c3", "Austin Scott");
    public static final Peer evan = makePeer("01:ca:d9:8b:31:8f", "Evan Vander Hoeven");
    public static final Peer nick = makePeer("30:fb:d7:95:1b:e1", "Nick Zolondek");
    public static final Peer nicholas = makePeer("e8:ab:21:89:58:06", "Nicholas LaBelle");

}
