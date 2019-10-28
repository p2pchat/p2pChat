package edu.uwstout.p2pchat.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class P2pRepository
{

    private DataAccessObject dao;

    private static class AsyncInsertPeer extends AsyncTask<Peer, Void, Void>
    {

        private DataAccessObject dao;

        public AsyncInsertPeer(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        @Override
        protected Void doInBackground(final Peer... peers)
        {
            dao.insertPeers(peers);
            return null;
        }
    }

    private static class AsyncDeletePeer extends AsyncTask<Peer, Void, Void>
    {

        private DataAccessObject dao;

        public AsyncDeletePeer(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        @Override
        protected Void doInBackground(final Peer... peers)
        {
            dao.deletePeers(peers);
            return null;
        }
    }

    private static class AsyncDeleteMessage extends AsyncTask<Message, Void, Void>
    {

        private DataAccessObject dao;

        public AsyncDeleteMessage(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        @Override
        protected Void doInBackground(final Message... messages)
        {
            dao.deleteMessages(messages);
            return null;
        }
    }

    private static class AsyncInsertMessage extends AsyncTask<Message, Void, Void>
    {

        private DataAccessObject dao;

        public AsyncInsertMessage(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        @Override
        protected Void doInBackground(final Message... messages)
        {
            dao.insertMessages(messages);
            return null;
        }
    }

    public final void insertPeers(final Peer... peers)
    {
        (new AsyncInsertPeer(dao)).execute(peers);
    }

    public final void insertMessages(final Message... messages)
    {
        (new AsyncInsertMessage(dao)).execute(messages);
    }

    public final List<Message> getMessages(final String macAddress)
    {
        return dao.getMessagesFromPeer(macAddress);
    }

    public final List<Peer> getPeers()
    {
        return dao.getPeers();
    }

    public final void deletePeers(final Peer... peers)
    {
        (new AsyncDeletePeer(dao)).execute(peers);
    }

    public final void deleteMessages(final Message... messages)
    {
        (new AsyncDeleteMessage(dao)).execute(messages);
    }

    public P2pRepository(final Application app)
    {
        P2pDatabase database = P2pDatabase.getInstance(app);
        dao = database.dataAccessObject();

    }
}
