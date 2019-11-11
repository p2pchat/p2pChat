package edu.uwstout.p2pchat.testing;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import edu.uwstout.p2pchat.ExternalFile;
import edu.uwstout.p2pchat.InMemoryFile;
import edu.uwstout.p2pchat.ViewModel;
import edu.uwstout.p2pchat.room.Message;
import edu.uwstout.p2pchat.room.Peer;

public class MockViewModel extends ViewModel {
    private static ArrayList<Peer> peers;
    private static ArrayList<Message> messages;

    private static ArrayList<MockLiveData<List<Peer>>> peerLiveData;
    private static ArrayList<MockLiveData<List<Message>>> messageLiveData;

    private static ArrayList<Peer> createPeers() {
        ArrayList<Peer> result = new ArrayList<Peer>();
        result.add(MockPeers.austin);
        result.add(MockPeers.evan);
        result.add(MockPeers.nicholas);
        result.add(MockPeers.nick);
        return result;
    }
    private static ArrayList<Message> createMessages() {
        ArrayList<Message> result = new ArrayList<Message>();
        result.add(MockMessages.austin1);
        result.add(MockMessages.austin2);
        result.add(MockMessages.austin3);
        result.add(MockMessages.austin4);
        return result;
    }
    public static void resetModel() {
        peers = createPeers();
        messages = createMessages();
        peerLiveData = new ArrayList<>();
        messageLiveData = new ArrayList<>();
    }

    private static void updatePeerLiveData() {
        for(MockLiveData<List<Peer>> mockLiveData : peerLiveData) {
            mockLiveData.update();
        }
    }

    private static void updateMessageLiveData() {
        for(MockLiveData<List<Message>> mockLiveData : messageLiveData) {
            mockLiveData.update();
        }
    }

    @Override
    public void insertPeer(String macAddress, String nickname) {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        peers.removeIf(new Predicate<Peer>() {
            @Override
            public boolean test(Peer p) {
                return p.macAddress == macAddress;
            }
        });
        peers.add(peer);
        updatePeerLiveData();
    }

    @Override
    public void insertMessage(String macAddress, Date timestamp, Boolean sent, String mimeType, String content) {
        Message message = new Message(macAddress);
        message.id = MockMessages.getCurrentID();
        message.timestamp = timestamp;
        message.sent = sent;
        message.mimeType = mimeType;
        message.content = content;

        messages.add(message);

        updateMessageLiveData();

    }

    @Override
    public void insertTextMessage(String macAddress, Date timestamp, Boolean sent, String message) {
        insertMessage(macAddress, timestamp, sent, "text/message", message);
    }

    @Override
    public Boolean insertFileMessage(String macAddress, Date timestamp, Boolean sent, InMemoryFile file, Context context) {
        ExternalFile externalFile = file.saveToStorage(context, timestamp);
        if(externalFile == null) return false;
        insertMessage(macAddress, timestamp, sent, externalFile.getMimeType(), externalFile.getPath());
        return true;
    }

    @Override
    public void deletePeer(String macAddress) {
        peers.removeIf(new Predicate<Peer>() {
            @Override
            public boolean test(Peer peer) {
                return peer.macAddress == macAddress;
            }
        });
        updatePeerLiveData();
    }

    @Override
    public void deleteMessage(int messageId) {
        messages.removeIf(new Predicate<Message>() {
            @Override
            public boolean test(Message message) {
                return message.id == messageId;
            }
        });
        updateMessageLiveData();
    }

    @Override
    public LiveData<List<Peer>> getPeers() {
        MockLiveData<List<Peer>> mockLiveData = new MockLiveData<>(new MockDataUpdater<List<Peer>>() {
            @Override
            public List<Peer> update() {
                return peers;
            }
        });
        peerLiveData.add(mockLiveData);
        return mockLiveData;
    }

    @Override
    public LiveData<List<Message>> getMessages(String macAddress) {
        MockLiveData<List<Message>> mockLiveData = new MockLiveData<>(new MockDataUpdater<List<Message>>() {
            @Override
            public List<Message> update() {
                ArrayList<Message> result = new ArrayList<>();
                for(Message message : messages) {
                    if(message.macAddress.equals(macAddress)) {
                        result.add(message);
                    }
                }
                return result;
            }
        });
        messageLiveData.add(mockLiveData);
        return mockLiveData;
    }

    public MockViewModel(Application app) {
        super(app);
        if(peers == null || messages == null) {
            resetModel();
        }
    }
}
