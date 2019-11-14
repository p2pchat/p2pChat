package edu.uwstout.p2pchat;

import static com.google.common.truth.Truth.*;

import org.junit.Test;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.uwstout.p2pchat.FileTransfer.ReceiverAsyncTask;
import edu.uwstout.p2pchat.FileTransfer.SendDataService;
import edu.uwstout.p2pchat.FileTransfer.UpdaterAsyncTask;

/**
 * Tests the serialization and deserialization
 * the InMemoryFile and InetAddress classes.
 * This functionality is essential for data
 * transfer and must be solid.
 */
public class SerializationTest
{
    /**
     * Checks that we can serialize and deserialize an
     * InMemoryFile without data loss.
     */
    @Test
    public void InMemoryFileSerialization()
    {
        // Create the InMemoryFile we want to serialize
        InMemoryFile imf = new InMemoryFile("Hello World!");
        // serialize it.
        InputStream inputStream = SendDataService.serializeInMemoryFile(imf);
        assertThat(inputStream).isNotNull();
        // deserialize it.
        InMemoryFile imfRetrieved = ReceiverAsyncTask.parseInMemoryFileFromInputStream(inputStream);
        assertThat(imfRetrieved).isNotNull();
        // Check that the contents were preserved.
        assertThat(imfRetrieved.getMimeType()).isEqualTo(imf.getMimeType());
        assertThat(imfRetrieved.getTextMessage()).isEqualTo(imf.getTextMessage());
    }

    @Test
    public void INetAddressSerialization()
    {
        try
        {
            // Create the INetAddress we want to serialize.
            byte[] ipAddr = new byte[]{127, 0, 0, 1};
            InetAddress address = InetAddress.getByAddress(ipAddr);
            // serialize it.
            InputStream inputStream = SendDataService.serializeINetAddress(address);
            assertThat(inputStream).isNotNull();
            // deserialize it.
            InetAddress recievedAddress =
                    UpdaterAsyncTask.parseInetAddressFromInputStream(inputStream);
            assertThat(recievedAddress).isNotNull();
            // Check that the contents were preserved.
            assertThat(recievedAddress.getCanonicalHostName())
                    .isEqualTo(address.getCanonicalHostName());
        }
        catch (UnknownHostException e)
        {
            assert(false);
        }
    }
}
