package edu.uwstout.p2pchat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.room.Message;
import edu.uwstout.p2pchat.room.P2pRepository;
import edu.uwstout.p2pchat.room.Peer;

public class ViewModel extends AndroidViewModel {

    private P2pRepository repo;

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void insertPeer(String macAddress, String nickname) {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        repo.insertPeers(peer);
    }

    public void insertMessage(String macAddress, Date timestamp, Boolean sent, String mimeType, String content) {
        Message message = new Message();
        message.macAddress = macAddress;
        message.timestamp = timestamp;
        message.sent = sent;
        message.mimeType = mimeType;
        message.content = content;
        repo.insertMessages(message);
    }

    public void deletePeer(String macAddress) {
        repo.deletePeers(new Peer(macAddress));
    }

    public void deleteMessage(int messageId) {
        Message message = new Message();
        message.id = messageId;
        repo.deleteMessages(message);
    }

    public LiveData<List<Peer>> getPeers() {
        return repo.getPeers();
    }

    public LiveData<List<Message>> getMessages(String macAddress) {
        return repo.getMessages(macAddress);
    }

}
