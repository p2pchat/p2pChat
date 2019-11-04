package edu.uwstout.p2pchat;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.room.Message;
import edu.uwstout.p2pchat.room.P2pRepository;
import edu.uwstout.p2pchat.room.Peer;

public class ViewModel extends AndroidViewModel
{

    private P2pRepository repo;

    public ViewModel(@NonNull final Application application)
    {
        super(application);
        repo = new P2pRepository(application);
    }

    public final void insertPeer(final String macAddress, final String nickname)
    {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        repo.insertPeers(peer);
    }

    public final void insertMessage(final String macAddress, final Date timestamp,
            final Boolean sent, final String mimeType,
            final String content)
    {
        Message message = new Message(macAddress);
        message.timestamp = timestamp;
        message.sent = sent;
        message.mimeType = mimeType;
        message.content = content;
        repo.insertMessages(message);
    }

    public final void insertTextMessage(final String macAddress, final Date timestamp,
            final Boolean sent, final String message)
    {
        Message newMessage = new Message(macAddress);
        newMessage.timestamp = timestamp;
        newMessage.sent = sent;
        newMessage.mimeType = "text/message";
        newMessage.content = message;
        repo.insertMessages(newMessage);
    }

    public final Boolean insertFileMessage(final String macAddress, final Date timestamp,
            final Boolean sent,
            final InMemoryFile file, final Context context)
    {
        ExternalFile storedFile = file.saveToStorage(context, timestamp);
        if (storedFile == null)
        {
            return false;
        }
        Message message = new Message(macAddress);
        message.mimeType = file.getMimeType();
        message.timestamp = timestamp;
        message.sent = sent;
        message.content = storedFile.getPath();
        repo.insertMessages(message);
        return true;
    }

    public final void deletePeer(final String macAddress)
    {
        repo.deletePeers(new Peer(macAddress));
    }

    public final void deleteMessage(final int messageId)
    {
        Message message = new Message("");
        message.id = messageId;
        repo.deleteMessages(message);
    }

    public final List<Peer> getPeers()
    {
        return repo.getPeers();
    }

    public final boolean peerExists(String address)
    {
        for (Peer peer : getPeers())
        {
            if (peer.macAddress.equals(address))
            {
                return true;
            }

        }
        return false;
    }

    public final List<Message> getMessages(final String macAddress)
    {
        return repo.getMessages(macAddress);
    }

}
