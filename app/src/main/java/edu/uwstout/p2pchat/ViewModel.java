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

public class ViewModel extends AndroidViewModel
{

    private P2pRepository repo;

    /**
     *
     * @param application Requires Application reference
     */
    public ViewModel(@NonNull final Application application)
    {
        super(application);
        repo = new P2pRepository(application);
    }

    /**
     *
     * @param macAddress Peer's Mac Address
     * @param nickname Peer's Nickname
     */
    public void insertPeer(final String macAddress, final String nickname)
    {
        Peer peer = new Peer(macAddress);
        peer.nickname = nickname;
        repo.insertPeers(peer);
    }

    /**
     *
     * @param macAddress Peer's Mac Address to create Message
     * @param timestamp Time of creation
     * @param sent message sender status
     * @param mimeType Type of content
     * @param content message content
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
     *
     * @param macAddress Peer's Mac Address to create Message
     * @param timestamp Time of creation
     * @param sent message sender status
     * @param message message content
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
     *
     * @param macAddress Peer's Mac Address to create Message
     * @param timestamp Time of creation
     * @param sent message sender status
     * @param file File
     * @param context Context of File
     * @return Boolean file state of existence
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
     *
     * @param macAddress Peer Address
     */
    public void deletePeer(final String macAddress)
    {
        repo.deletePeers(new Peer(macAddress));
    }

    /**
     *
     * @param messageId Message Id
     */
    public void deleteMessage(final int messageId)
    {
        Message message = new Message("");
        message.id = messageId;
        repo.deleteMessages(message);
    }

    /**
     *
     * @return LiveData containing a list of peers
     */
    public LiveData<List<Peer>> getPeers()
    {
        return repo.getPeers();
    }

    /**
    * Wipes the database for a clean slate.
    */
    public void deleteEverything() {
        repo.deleteEverything();
    }

    /**
     *
     * @param macAddress Peer's MacAddress
     * @return LiveData containing a list of messages
     */
    public LiveData<List<Message>> getMessages(final String macAddress)
    {
        return repo.getMessages(macAddress);
    }

}
