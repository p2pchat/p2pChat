package edu.uwstout.p2pchat.WifiDirectHelpers;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import edu.uwstout.p2pchat.InMemoryFile;
import edu.uwstout.p2pchat.ViewModel;
import edu.uwstout.p2pchat.WifiDirect;

/**
 * A simple server socket that accepts connections and writes
 * some data on the stream.
 * Only applicable when this device is acting as the server.
 *
 * @author VanderHoevenEvan (Evan Vander Hoeven)
 */
public class ReceiverAsyncTask extends AsyncTask<InMemoryFileReceivedListener, Object, InMemoryFile>
{
    /**
     * Magic number for open port.
     */
    public static final int MAGIC_PORT = 8988;
    /**
     * The tag for logging.
     */
    private static final String LOG_TAG = "ReceiverAsyncTask";
    /**
     * Keeps track of all listeners passed into the asynchronous task.
     */
    @VisibleForTesting
    protected static ArrayList<InMemoryFileReceivedListener> listeners;

    /**
     * Listens for incoming messages in the background.
     *
     * @param inMemoryFileReceivedListeners
     *         A series of InMemoryFileReceivedListeners which want to be notified when an
     *         InMemoryFile is received from our peer.
     * @return An InMemoryFile which was sent to us by our peer.
     */
    @Override
    protected InMemoryFile doInBackground(
            InMemoryFileReceivedListener... inMemoryFileReceivedListeners)
    {
        listeners = new ArrayList<>();
        listeners.addAll(Arrays.asList(inMemoryFileReceivedListeners));
        try
        {
            ServerSocket serverSocket = new ServerSocket(MAGIC_PORT);
            Log.i(LOG_TAG, "Receiver: Socket opened");
            /*
                The call to accept is where the black magic of accepting
                a connection from a client happens. This method call is
                blocking, which is why this must be
                done in a separate thread.
            */
            Socket client = serverSocket.accept();
            // Now that we have passed that line,
            // we have accepted a connection.
            Log.i(LOG_TAG, "Receiver: Connection established");
            // get the InMemoryFile object from the InputStream
            return parseInMemoryFileFromInputStream(client.getInputStream());
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error receiving data: " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e(LOG_TAG, "InputStream did not contain InMemoryFile. " + e.getMessage());
        }
        return null;
    }

    /**
     * Notifies all passed in InMemoryFileReceivedListeners of the message received.
     * @param inMemoryFile the message sent by the our peer.
     */
    @Override
    protected void onPostExecute(InMemoryFile inMemoryFile)
    {
        if (inMemoryFile == null)
            return;
        for (InMemoryFileReceivedListener listener: listeners)
        {
            listener.onInMemoryFileAvailable(inMemoryFile);
        }
    }

    /**
     * Converts an inputStream into an InMemoryFile object.
     *
     * @param inputStream
     *         An InputStream which contains a serialized InMemoryFile object.
     * @return an InMemoryFile which came from the parameter InputStream.
     * @throws IOException
     *         if the parameter InputStream could not be opened.
     * @throws ClassNotFoundException
     *         if the InputStream does not contain an InMemoryFile.
     */
    public static InMemoryFile parseInMemoryFileFromInputStream(InputStream inputStream)
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        InMemoryFile imf = (InMemoryFile) ois.readObject();
        ois.close();
        return imf;
    }
}