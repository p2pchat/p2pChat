package edu.uwstout.p2pchat.FileTransfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.uwstout.p2pchat.WifiDirect;

public class UpdaterAsyncTask extends AsyncTask
{
    /**
     * Application context needed to do tasks.
     */ // suppress warnings related to context leak.
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    /**
     * Magic number for open port.
     */
    public static final int MAGIC_PORT = 8989;
    /**
     * The tag for logging.
     */
    private static final String LOG_TAG = "UpdaterAsyncTask";

    /**
     * Non-Default Constructor
     * @param c Application context
     */
    public UpdaterAsyncTask(final Context c)
    {
        this.context = c.getApplicationContext();
    }

    /**
     * Listens for incoming messages in the background.
     * @param objects Required by the superclass, these are unnecessary.
     * @return An object. Currently null.
     */
    @Override
    protected Object doInBackground(final Object[] objects)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(MAGIC_PORT);
            Log.i(LOG_TAG, "Updater: Socket opened");
            // The call to accept is blocking, which is why this
            // is in a separate thread.
            Socket client = serverSocket.accept();
            // When we pass that line, we have accepted a connection.
            Log.i(LOG_TAG, "Updater: Connection Established");
            InputStream inputStream = client.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            // get the INetAddress object from the InputStream
            InetAddress clientLocalHost;
            try
            {
                clientLocalHost = (InetAddress) ois.readObject();
            }
            catch (ClassNotFoundException e)
            {
                Log.d(LOG_TAG, "Could not convert input stream to InetAddress");
                Log.e(LOG_TAG, "ClassNotFoundException: " + e.getMessage());
                return null;
            }
            // Update the singleton with the new Client information.
            Log.i(LOG_TAG,
                    "Client information received. " + clientLocalHost.getCanonicalHostName());
            WifiDirect.getInstance(this.context).setClient(clientLocalHost);
            return null;
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error receiving data: " + e.getMessage());
            return null;
        }
    }
}
