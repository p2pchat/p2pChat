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
        InputStream inputStream = null;
        // If the Bad Message isn't overwritten by deserialization, we assert false.
        InMemoryFile imfRetrieved = new InMemoryFile("Bad Message!");
        try
        {
            // serialize it.
            inputStream = SendDataService.serializeInMemoryFile(imf);
            assertThat(inputStream).isNotNull();
        }
        catch (Exception e)
        {
            assert(false); // exceptions are bad and should feel bad.
        }
        try
        {
            // deserialize it.
            imfRetrieved = ReceiverAsyncTask.parseInMemoryFileFromInputStream(inputStream);
            assertThat(imfRetrieved).isNotNull();
        }
        catch (Exception e)
        {
            assert(false); // separate try/catch tells us where the error is occuring.
        }
        // Check that the contents were preserved.
        assertThat(imfRetrieved.getMimeType()).isEqualTo(imf.getMimeType());
        assertThat(imfRetrieved.getTextMessage()).isEqualTo(imf.getTextMessage());
    }

    /**
     * Checks that we can serialize and deserialize an
     * InetAddress without data loss.
     */
    @Test
    public void INetAddressSerialization()
    {
        // Create the INetAddress we want to serialize.
        byte[] ipAddr = new byte[]{127, 0, 0, 1};
        InetAddress address = null;
        InputStream inputStream = null;
        InetAddress receivedAddress = null;
        try
        {
            address = InetAddress.getByAddress(ipAddr);
        }
        catch (UnknownHostException e)
        {
            // This isn't a good error, because the test fails
            // before we get to our own code.
            assert(false);
        }
        try
        {
            // serialize it.
            inputStream = SendDataService.serializeINetAddress(address);
            assertThat(inputStream).isNotNull();
        }
        catch (Exception e)
        {
            assert(false); // exceptions are inherently bad in testing
        }

        try
        {
            // deserialize it.
            receivedAddress = UpdaterAsyncTask.parseInetAddressFromInputStream(inputStream);
            assertThat(receivedAddress).isNotNull();
        }
        catch (Exception e)
        {
            assert(false);
        }
        // Check that the contents were preserved.
        assertThat(receivedAddress.getCanonicalHostName())
                .isEqualTo(address.getCanonicalHostName());
    }
}
