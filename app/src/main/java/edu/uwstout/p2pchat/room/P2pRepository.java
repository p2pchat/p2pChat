package edu.uwstout.p2pchat.room;

import android.app.Application;
import android.os.AsyncTask;

public class P2pRepository {

    private DataAccessObject dao;

    private class AsyncInsertPeer extends AsyncTask<Peer, Void, Void> {

        private DataAccessObject dao;

        public AsyncInsertPeer(DataAccessObject dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Peer... peers) {
            dao.insertPeers(peers);
            return null;
        }
    }

    private class AsyncInsertMessage extends AsyncTask<Message, Void, Void> {

        private DataAccessObject dao;

        public AsyncInsertMessage(DataAccessObject dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            dao.insertMessages(messages);
            return null;
        }
    }

    public void insertPeers(Peer... peers) {
        (new AsyncInsertPeer(dao)).execute(peers);
    }

    public void insertMessages(Message... messages) {
        (new AsyncInsertMessage(dao)).execute(messages);
    }

    public P2pRepository(Application app) {
        P2pDatabase database = P2pDatabase.getInstance(app);
        dao = database.dataAccessObject();

    }
}
