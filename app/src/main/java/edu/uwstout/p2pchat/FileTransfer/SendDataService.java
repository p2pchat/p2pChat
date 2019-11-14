package edu.uwstout.p2pchat.FileTransfer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
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
 *
 * @author VanderHoevenEvan (Evan Vander Hoeven)
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
     * @param intent
     *         An Intent that contains information that we want to send.
     * @see IntentService#onHandleIntent(Intent)
     */
    @SuppressWarnings("checkstyle:LineLength")
    @Override
    protected void onHandleIntent(@Nullable final Intent intent)
    {
        if (intent == null)
        {
            // Can't handle the intent if it is null.
            return;
        }
        try
        {
            if (Objects.equals(intent.getAction(), ACTION_SEND_DATA))
            {
                transmitInMemoryFile(intent);
            }
            else if (Objects.equals(intent.getAction(), ACTION_UPDATE_INETADDRESS))
            {
                transmitINetAddress(intent);
            }
            else
            {
                Log.e(LOG_TAG, "Unrecognized action: " + intent.getAction());
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
     * Sends an InMemoryFile to a peer given information about
     * how to communicate with the peer.
     *
     * @param intent
     *         a non-null intent whose action is ACTION_SEND_DATA.
     */
    private void transmitInMemoryFile(@NonNull final Intent intent)
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
            InputStream inputStream = serializeInMemoryFile(IMF);

            // this is where the data is sent
            if (inputStream != null)
            {
                transferStreams(inputStream, outputStream);
            }
            Log.d(LOG_TAG, "Client: Data Written");
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

    /**
     * Sends an INetAddress to the host (assume this device
     * is the client). So that the host knows how to communicate
     * with this device.
     *
     * @param intent
     *         a non-null intent whose action
     *         is ACTION_UPDATE_INETADDRESS.
     */
    private void transmitINetAddress(@NonNull final Intent intent)
    {
        // Get the localhost to send to the host server.
        final InetAddress LOCALHOST =
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
            InputStream inputStream = serializeINetAddress(LOCALHOST);
            // this is where the data is sent
            if (inputStream != null)
            {
                transferStreams(inputStream, outputStream);
            }
            Log.d(LOG_TAG, "Client: Data written for host update");

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

    /**
     * Converts an InMemoryFile into an InputStream
     *
     * @param imf
     *         an InMemoryFile that we wish to serialize.
     * @return an InputStream with a serialized InMemoryFile,
     * or null if an error occurred.
     */
    public static InputStream serializeInMemoryFile(InMemoryFile imf)
    {
        try
        {
            InputStream inputStream = new FileInputStream("imf.ser");
            inputStream = new BufferedInputStream(inputStream);
            inputStream = new ObjectInputStream(inputStream);
            return inputStream;
        }
        catch (FileNotFoundException e)
        {
            Log.e(LOG_TAG, "404 Error: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error creating ObjectInputStream: " + e.getMessage());
        }
        return null;
    }

    /**
     * Converts an InetAddress into an InputStream
     *
     * @param address
     *         an InetAddress that we wish to serialize
     * @return an InputStream with a serialized InetAddress,
     * or null if an error occurred.
     */
    public static InputStream serializeINetAddress(InetAddress address)
    {
        try
        {
            InputStream inputStream = new FileInputStream("address.ser");
            inputStream = new BufferedInputStream(inputStream);
            inputStream = new ObjectInputStream(inputStream);
            return inputStream;
        }
        catch (FileNotFoundException e)
        {
            Log.e(LOG_TAG, "404 Error: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error creating ObjectInputStream: " + e.getMessage());
        }
        return null;
    }

    /**
     * Writes the contents of the InputStream to the OutputStream.
     *
     * @param in
     *         The InputStream full of data.
     * @param out
     *         The OutputStream that needs the data.
     */
    public static void transferStreams(final InputStream in, final OutputStream out)
    {
        final int MAGIC_NUMBER = 1024;
        byte[] buf = new byte[MAGIC_NUMBER];
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
