package edu.uwstout.p2pchat.FileTransfer;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import edu.uwstout.p2pchat.InMemoryFile;

/**
 * A service that processes each file transfer request (Android Intent)
 * by opening a socket connection with the WiFi Direct Group owner and
 * writing the file.
 *
 * TODO is this sucker one-way? cause that needs to change!
 */
final class FileTransferService extends IntentService
{
    /**
     * A tag for logging
     */
    private static final String LOG_TAG = "FileTransferService";
    /**
     * A time limit for the socket.
     */
    private static final int SOCKET_TIMEOUT = 5000; // five seconds
    /**
     * A definition for the action our intents will use.
     */
    public static final String ACTION_SEND_FILE = "edu.uwstout.p2pchat.SEND_FILE";
    /**
     * provides a
     */
    public static final String EXTRAS_IN_MEMORY_FILE = "imf";
    /**
     * I don't know what this one does either.
     * TODO figure that out.
     */
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    /**
     * Nor this one.
     * TODO figure that out.
     */
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";

    /**
     * Default constructor.
     */
    FileTransferService()
    {
        super("FileTransferService");
    }

    /**
     * Non-default constructor.
     *
     * @param name
     *         Used to name the worker thread, useful only for debugging.
     */
    FileTransferService(final String name)
    {
        super(name);
    }

    /**
     * Handle an intent sent from within this class.
     *
     * @param intent
     *         *shrugs* only god knows.
     * @see IntentService#onHandleIntent(Intent)
     */
    @Override
    protected void onHandleIntent(@Nullable final Intent intent)
    {
        try
        {
            if (intent != null && Objects.equals(intent.getAction(), ACTION_SEND_FILE))
            {
                final InMemoryFile IMF =
                        Objects.requireNonNull(intent.getExtras())
                                .getParcelable(EXTRAS_IN_MEMORY_FILE);
                String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
                Socket socket = new Socket();
                final int PORT = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

                try
                {
                    Log.d(LOG_TAG, "Opening client socket");
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(host, PORT)), SOCKET_TIMEOUT);

                    Log.d(LOG_TAG, "Client socket connected: " + socket.isConnected());
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = null;

                    try
                    {
                        // We go through these steps to get to an ObjectInputStream.
                        // Don't question it, I won't have an answer.
                        inputStream = new FileInputStream("IMF.ser");
                        inputStream = new BufferedInputStream(inputStream);
                        inputStream = new ObjectInputStream(inputStream);
                        // this is where the data is sent
                        copyFile(inputStream, outputStream);
                        Log.d(LOG_TAG, "Client: Data Written");
                    }
                    catch (FileNotFoundException e)
                    {
                        Log.e(LOG_TAG, "404 Error: " + e.getMessage());
                    }
                }
                catch (IOException e)
                {
                    Log.e(LOG_TAG, "Error: " + e.getMessage());
                }
                finally
                {
                    if (socket.isConnected())
                    {
                        try
                        {
                            socket.close();
                        }
                        catch (IOException e)
                        {
                            // Give up all hope, ye who enter this block!
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (NullPointerException npe)
        {
            Log.e(LOG_TAG, "Null pointer while handling an intent.");
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG,
                    "Error while handling an intent: " + e.getMessage()
                            + "Caused by: " + e.getCause());
        }
    }

    public static void copyFile(InputStream inputStream, OutputStream out)
    {
        byte[] buf = new byte[1024];
        int len;
        try
        {
            while ((len = inputStream.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG, e.toString());
        }
    }
}
