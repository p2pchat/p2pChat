package edu.uwstout.p2pchat;

import android.app.Application;
import android.content.Context;

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
        Message message = new Message(macAddress);
        message.timestamp = timestamp;
        message.sent = sent;
        message.mimeType = mimeType;
        message.content = content;
        repo.insertMessages(message);
    }

    public void insertTextMessage(String macAddress, Date timestamp, Boolean sent, String message) {
        Message newMessage = new Message(macAddress);
        newMessage.timestamp = timestamp;
        newMessage.sent = sent;
        newMessage.mimeType = "text/message";
        newMessage.content = message;
        repo.insertMessages(newMessage);
    }

    public Boolean insertFileMessage(String macAddress, Date timestamp, Boolean sent, InMemoryFile file, Context context) {
        ExternalFile storedFile = file.saveToStorage(context, timestamp);
        if(storedFile==null) return false;
        Message message = new Message(macAddress);
        message.mimeType = file.mimeType;
        message.timestamp = timestamp;
        message.sent = sent;
        message.content = storedFile.getPath();
        repo.insertMessages(message);
        return true;
    }

    public void deletePeer(String macAddress) {
        repo.deletePeers(new Peer(macAddress));
    }

    public void deleteMessage(int messageId) {
        Message message = new Message("");
        message.id = messageId;
        repo.deleteMessages(message);
    }

    public List<Peer> getPeers() {
        return repo.getPeers();
    }

    public List<Message> getMessages(String macAddress) {
        return repo.getMessages(macAddress);
    }

}
