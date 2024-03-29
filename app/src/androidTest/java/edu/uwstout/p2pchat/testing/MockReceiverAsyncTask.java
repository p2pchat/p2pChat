package edu.uwstout.p2pchat.testing;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uwstout.p2pchat.InMemoryFile;
import edu.uwstout.p2pchat.WifiDirectHelpers.InMemoryFileReceivedListener;
import edu.uwstout.p2pchat.WifiDirectHelpers.ReceiverAsyncTask;

public final class MockReceiverAsyncTask extends ReceiverAsyncTask
{
    /**
     * The mock behavior always returns this mock file,
     * though it has a setter if certain tests request it.
     */
    private static InMemoryFile mockFile = new InMemoryFile("In the beginning God created the universe. This was "
            + "widely regarded as a very bad move.");
    /**
     * Because the implementation is setup to receive an infinite number
     * of text messages, we don't want to flood our database with test
     * messages. So, we pass in the number of messages that we want to
     * fake before we simply make the MockReceiverAsyncTask unresponsive
     * and never use the callbacks again.
     */
    public static int numberOfMessages = 1;

    /**
     * Setter for the mock InMemory file for comparison in our tests.
     */
    public static synchronized void setMockFile(InMemoryFile imf)
    {
        mockFile = imf;
    }

    /**
     * Getter for the mock InMemoryFile for comparison in our tests.
     * @return The InMemoryFile which this AsyncTask sends to InMemoryFileReceivedListeners.
     */
    public static synchronized InMemoryFile getMockFile()
    {
        return mockFile;
    }

    /**
     * Asynchronously fakes an InMemoryFile to mock the parent class behavior.
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
        return (numberOfMessages-- > 0) ? mockFile : null;
    }

    /**
     * Notifies all passed in InMemoryFileReceivedListeners of the message received.
     *
     * @param inMemoryFile
     *         the message sent by the our peer.
     */
    @Override
    protected void onPostExecute(InMemoryFile inMemoryFile)
    {
        if (inMemoryFile != null)
        {
            super.onPostExecute(inMemoryFile);
        } // Else, do nothing, never return.
    }
}
