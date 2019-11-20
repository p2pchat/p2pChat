package edu.uwstout.p2pchat.WifiDirectHelpers;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class ReceiverAsyncTask extends AsyncTask
{
    /**
     * Application context needed to do tasks.
     */ // suppress warnings related to context leak.
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    /**
     * Magic number for open port.
     */
    public static final int MAGIC_PORT = 8988;
    /**
     * The tag for logging.
     */
    private static final String LOG_TAG = "ReceiverAsyncTask";

    /**
     * Non-Default Constructor.
     *
     * @param c
     *         Application context.
     */
    public ReceiverAsyncTask(final Context c)
    {
        this.context = c.getApplicationContext();
    }

    /**
     * Listens for incoming messages in the background.
     *
     * @param objects
     *         Required by the superclass, these are unnecessary.
     * @return An object. Currently null.
     */
    @Override
    protected Object doInBackground(final Object[] objects)
    {
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
            InMemoryFile imf = parseInMemoryFileFromInputStream(client.getInputStream());
            // save the InMemoryFile to the database.
            // determine if we are dealing with a text message or a file.
            if (imf != null)
            {
                Log.i(LOG_TAG, "Message received, MIME type: " + imf.getMimeType());
                if (imf.getMimeType().equals(InMemoryFile.MESSAGE_MIME_TYPE))
                {
                    // add a text message to the database
                    String macAddress = WifiDirect.getInstance(this.context)
                            .getPeerDevice().deviceAddress;
                    new ViewModel((Application) this.context.getApplicationContext())
                            .insertTextMessage(macAddress, new Date(), false, imf.getTextMessage());
                }
                else
                {
                    // add a regular file to the database
                    String macAddress = WifiDirect.getInstance(this.context)
                            .getPeerDevice().deviceAddress;
                    new ViewModel((Application) this.context.getApplicationContext())
                            .insertFileMessage(macAddress, new Date(), false, imf, this.context);
                }
            }
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