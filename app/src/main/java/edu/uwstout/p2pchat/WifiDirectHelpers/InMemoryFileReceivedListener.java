package edu.uwstout.p2pchat.WifiDirectHelpers;

import edu.uwstout.p2pchat.InMemoryFile;

public interface InMemoryFileReceivedListener
{
    /**
     * Notifies the listener that an InMemoryFile is available
     * @param inMemoryFile A new InMemoryFile
     */
    public void onInMemoryFileAvailable(InMemoryFile inMemoryFile);
}
