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

/**
 * Helper class for interacting with the Model
 */
public class ViewModel extends AndroidViewModel
{

    private P2pRepository repo;

    /**
     * Constructor
     * @param application
     */
    public ViewModel(@NonNull final Application application)
    {
        super(application);
        repo = new P2pRepository(application);
    }

    /**
     * Inserts peer into the database safely in background
     */
    public void insertPeer(final String macAddress, final String nickname)
    {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        repo.insertPeers(peer);
    }

    /**
     * Inserts a message into the database
     * @param macAddress
     * @param timestamp
     * @param sent
     * @param mimeType
     * @param content
     */
    public void insertMessage(final String macAddress, final Date timestamp,
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

    /**
     * Inserts a text message into the database
     * @param macAddress
     * @param timestamp
     * @param sent
     * @param message
     */
    public void insertTextMessage(final String macAddress, final Date timestamp,
            final Boolean sent, final String message)
    {
        Message newMessage = new Message(macAddress);
        newMessage.timestamp = timestamp;
        newMessage.sent = sent;
        newMessage.mimeType = "text/message";
        newMessage.content = message;
        repo.insertMessages(newMessage);
    }

    /**
     * Inserts a file message into the database
     * @param macAddress
     * @param timestamp
     * @param sent
     * @param file
     * @param context
     * @return
     */
    public Boolean insertFileMessage(final String macAddress, final Date timestamp,
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

    /**
     * Deletes the peer associated with macAddress from the database
     * @param macAddress
     */
    public void deletePeer(final String macAddress)
    {
        repo.deletePeers(new Peer(macAddress));
    }

    /**
     * Deletes the message associated with messageId from the database
     * @param messageId
     */
    public void deleteMessage(final int messageId)
    {
        Message message = new Message("");
        message.id = messageId;
        repo.deleteMessages(message);
    }

    /**
     * Gets a LiveData of all peers in the database
     * @return
     */
    public LiveData<List<Peer>> getPeers()
    {
        return repo.getPeers();
    }

    /**
     * Checks if a peer exists. Should not be called.
     * @param address
     * @return
     */
    public final boolean peerExists(String address)
    {
        List<Peer> peers = getPeers().getValue();
        if(peers == null) return false;
        for (Peer peer : getPeers().getValue())
        {
            if (peer.macAddress.equals(address))
            {
                return true;
            }

        }
        return false;
    }

    /**
     * Returns a LiveData of all messages associated with a peer's macAddress
     * @param macAddress
     * @return
     */
    public LiveData<List<Message>> getMessages(final String macAddress)
    {
        return repo.getMessages(macAddress);
    }

}
