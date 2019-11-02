package edu.uwstout.p2pchat;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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
     * Yeah, I don't know what this one does yet.
     * TODO figure that out.
     */
    public static final String EXTRAS_FILE_PATH = "file_url";
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
     * @param name Used to name the worker thread, useful only for debugging.
     */
    FileTransferService(final String name)
    {
        super(name);
    }

    /**
     * Handle an intent sent from within this class.
     * @param intent *shrugs* only god knows.
     * @see IntentService#onHandleIntent(Intent)
     */
    @Override
    protected void onHandleIntent(@Nullable final Intent intent)
    {
        Context context = getApplicationContext();
        try
        {
            if (intent != null && intent.getAction().equals(ACTION_SEND_FILE))
            {
                String fileUri = intent.getExtras().getString(EXTRAS_FILE_PATH);
                String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
                Socket socket = new Socket();
                final int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

                try
                {
                    Log.d(LOG_TAG, "Opening client socket");
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

                    Log.d(LOG_TAG, "Client socket connected: " + socket.isConnected());
                    OutputStream outputStream = socket.getOutputStream();
                    ContentResolver contentResolver = context.getContentResolver();
                    InputStream inputStream = null;

                    try
                    {
                        inputStream = contentResolver.openInputStream(Uri.parse(fileUri));
                    }
                    catch (FileNotFoundException e)
                    {
                        Log.e(LOG_TAG, "404 Error: " + e.getMessage());
                    }

                    /*
                        This is where I ought to turn that input stream
                        into an in memory file...
                        I think?
                     */
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
}
