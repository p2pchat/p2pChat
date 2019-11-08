package edu.uwstout.p2pchat.FileTransfer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import edu.uwstout.p2pchat.InMemoryFile;

/**
 * A service that processes each send data request (Android Intent)
 * by opening a socket connection with the WiFi Direct Group owner and
 * writing the file.
 */
public final class SendDataService extends IntentService
{
    /**
     * A tag for logging.
     */
    private static final String LOG_TAG = "SendDataService";
    /**
     * A time limit for the socket.
     */
    private static final int SOCKET_TIMEOUT = 5000; // five seconds
    /**
     * A definition for the action our intents
     * will use to transmit text messages.
     */
    public static final String ACTION_SEND_DATA = "edu.uwstout.p2pchat.SEND_DATA";
    /**
     * A definition for the action our intents
     * will use to update the host of client
     * information so that two-way communication
     * is possible.
     */
    public static final String ACTION_UPDATE_INETADDRESS = "edu.uwstout.p2pchat.UPDATE_INETADDRESS";
    /**
     * provides a constant for accessing the
     * InMemoryFile parceled in the intent.
     */
    public static final String EXTRAS_IN_MEMORY_FILE = "imf";
    /**
     * Provides a constant for accessing the INetAddress
     * parceled in the intent
     */
    public static final String EXTRAS_INETADDRESS = "INetAddress";
    /**
     * A constant for accessing information about the host we send data to.
     */
    public static final String EXTRAS_PEER_ADDRESS = "go_address";
    /**
     * A constant for accessing information about the port we send data to.
     */
    public static final String EXTRAS_PEER_PORT = "go_port";

    /**
     * Default constructor.
     */
    SendDataService()
    {
        super("SendDataService");
    }

    /**
     * Non-default constructor.
     *
     * @param name
     *         Used to name the worker thread, useful only for debugging.
     */
    SendDataService(final String name)
    {
        super(name);
    }

    /**
     * Handle an intent sent from within this class.
     *
     * @param intent An Intent that contains information that we want to send.
     * @see IntentService#onHandleIntent(Intent)
     */
    @SuppressWarnings("checkstyle:LineLength")
    @Override
    protected void onHandleIntent(@Nullable final Intent intent)
    {
        try
        {
            if (intent != null)
            {
                if (Objects.equals(intent.getAction(), ACTION_SEND_DATA))
                {
                    final InMemoryFile IMF =
                            Objects.requireNonNull(intent.getExtras())
                                    .getParcelable(EXTRAS_IN_MEMORY_FILE);
                    String host = intent.getExtras().getString(EXTRAS_PEER_ADDRESS);
                    Socket socket = new Socket();
                    final int PORT = intent.getExtras().getInt(EXTRAS_PEER_PORT);

                    try
                    {
                        Log.d(LOG_TAG, "Opening client socket");
                        socket.bind(null);
                        socket.connect((new InetSocketAddress(host, PORT)), SOCKET_TIMEOUT);

                        Log.d(LOG_TAG, "Client socket connected: " + socket.isConnected());
                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = null;

                        //noinspection CheckStyle
                        try
                        {
                            // We go through these steps to get to an ObjectInputStream.
                            // Don't question it, I won't have an answer.
                            inputStream = new FileInputStream("IMF.ser");
                            inputStream = new BufferedInputStream(inputStream);
                            inputStream = new ObjectInputStream(inputStream);
                            // this is where the data is sent
                            transferStreams(inputStream, outputStream);
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
                else if (Objects.equals(intent.getAction(), ACTION_UPDATE_INETADDRESS))
                {
                    // Get the localhost to send to the host server.
                    final InMemoryFile LOCALHOST =
                            Objects.requireNonNull(intent.getExtras())
                                    .getParcelable(EXTRAS_INETADDRESS);
                    String host = intent.getExtras().getString(EXTRAS_PEER_ADDRESS);
                    Socket socket = new Socket();
                    final int PORT = intent.getExtras().getInt(EXTRAS_PEER_PORT);

                    try
                    {
                        Log.d(LOG_TAG, "Opening client socket for host update");
                        socket.bind(null);
                        socket.connect((new InetSocketAddress(host, PORT)), SOCKET_TIMEOUT);

                        Log.d(LOG_TAG, "Client socket connected: " + socket.isConnected());
                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = null;

                        //noinspection CheckStyle
                        try
                        {
                            // We go through these steps to get to an ObjectInputStream.
                            // Don't question it, I won't have an answer.
                            inputStream = new FileInputStream("LOCALHOST.ser");
                            inputStream = new BufferedInputStream(inputStream);
                            inputStream = new ObjectInputStream(inputStream);
                            // this is where the data is sent
                            transferStreams(inputStream, outputStream);
                            Log.d(LOG_TAG, "Client: Data written for host update");
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
                else
                {
                    Log.e(LOG_TAG, "Unrecognized action: " + intent.getAction());
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

    /**
     * Writes the contents of the InputStream to the OutputStream
     * @param in The InputStream full of data.
     * @param out The OutputStream that needs the data.
     */
    public static void transferStreams(final InputStream in, final OutputStream out)
    {
        byte[] buf = new byte[1024];
        int len;
        try
        {
            while ((len = in.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG, e.toString());
        }
    }
}
