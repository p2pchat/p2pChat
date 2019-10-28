package edu.uwstout.p2pchat.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

/**
 * Class for performing interactions with the Database.
 */
public class P2pRepository
{

    /**
     * Reference to the DataAccessObject to be used in CRUD operations with the Database.
     */
    private DataAccessObject dao;

    /**
     * Helper class for inserting peers.
     */
    private static class AsyncInsertPeer extends AsyncTask<Peer, Void, Void>
    {

        /**
         * Reference to DataAccessObject for CRUD operations
         */
        private DataAccessObject dao;

        /**
         * @param daoRef Reference to Database's DataAccessObject
         */
        public AsyncInsertPeer(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        /**
         * Function that performs peer insertion on separate thread in background.
         * @param peers Peers to be inserted.
         * @return Nothing
         */
        @Override
        protected Void doInBackground(final Peer... peers)
        {
            dao.insertPeers(peers);
            return null;
        }
    }

    /**
     * Helper class for deleting peers
     */
    private static class AsyncDeletePeer extends AsyncTask<Peer, Void, Void>
    {

        /**
         * Reference to DataAccessObject for CRUD operations
         */
        private DataAccessObject dao;

        /**
         * @param daoRef Reference to Database's DataAccessObject
         */
        public AsyncDeletePeer(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        /**
         * Function that performs peer deletion on separate thread in background.
         * @param peers Peers to be deleted.
         * @return Nothing
         */
        @Override
        protected Void doInBackground(final Peer... peers)
        {
            dao.deletePeers(peers);
            return null;
        }
    }

    /**
     * Helper class for deleting messages
     */
    private static class AsyncDeleteMessage extends AsyncTask<Message, Void, Void>
    {

        /**
         * Reference to DataAccessObject for CRUD operations
         */
        private DataAccessObject dao;

        /**
         * @param daoRef Reference to Database's DataAccessObject
         */
        public AsyncDeleteMessage(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        /**
         * Function that performs message deletion on separate thread in background.
         * @param messages Messages to be deleted.
         * @return Nothing
         */
        @Override
        protected Void doInBackground(final Message... messages)
        {
            dao.deleteMessages(messages);
            return null;
        }
    }

    /**
     * Helper class for inserting messages
     */
    private static class AsyncInsertMessage extends AsyncTask<Message, Void, Void>
    {

        /**
         * Reference to DataAccessObject for CRUD operations
         */
        private DataAccessObject dao;

        /**
         * @param daoRef Reference to Database's DataAccessObject
         */
        public AsyncInsertMessage(final DataAccessObject daoRef)
        {
            dao = daoRef;
        }

        /**
         * Function that performs Message insertion on separate thread in background.
         * @param messages Messages to be inserted.
         * @return Nothing
         */
        @Override
        protected Void doInBackground(final Message... messages)
        {
            dao.insertMessages(messages);
            return null;
        }
    }

    /**
     * Inserts peers into the database safely in background
     * @param peers Peers to be inserted.
     */
    public final void insertPeers(final Peer... peers)
    {
        (new AsyncInsertPeer(dao)).execute(peers);
    }

    /**
     * Inserts messages into the database safely in background
     * @param messages Messages to be inserted.
     */
    public final void insertMessages(final Message... messages)
    {
        (new AsyncInsertMessage(dao)).execute(messages);
    }

    /**
     * Returns all messages associated with a peer's MAC Address
     * @param macAddress MAC Address of peer.
     * @return List of Messages
     */
    public final List<Message> getMessages(final String macAddress)
    {
        return dao.getMessagesFromPeer(macAddress);
    }

    /**
     * Returns list of all Peers in database
     * @return List of Peers
     */
    public final List<Peer> getPeers()
    {
        return dao.getPeers();
    }

    /**
     * Deletes peers safely in the background
     * @param peers Peers to be deleted
     */
    public final void deletePeers(final Peer... peers)
    {
        (new AsyncDeletePeer(dao)).execute(peers);
    }

    /**
     * Deletes messages safely in the background
     * @param messages Messages to be deleted
     */
    public final void deleteMessages(final Message... messages)
    {
        (new AsyncDeleteMessage(dao)).execute(messages);
    }

    /**
     * Repository constructor
     * @param app Application reference
     */
    public P2pRepository(final Application app)
    {
        P2pDatabase database = P2pDatabase.getInstance(app);
        dao = database.dataAccessObject();

    }
}
