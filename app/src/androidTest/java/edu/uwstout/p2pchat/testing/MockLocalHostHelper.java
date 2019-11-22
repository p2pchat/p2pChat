package edu.uwstout.p2pchat.testing;

import java.net.InetAddress;
import java.util.Arrays;

import edu.uwstout.p2pchat.WifiDirectHelpers.InetAddressListener;
import edu.uwstout.p2pchat.WifiDirectHelpers.LocalHostHelper;

public final class MockLocalHostHelper extends LocalHostHelper
{
    private final InetAddress mockLocalHost;

    /**
     * Non-Default constructor.
     */
    public MockLocalHostHelper()
    {
        this.mockLocalHost = InetAddress.getLoopbackAddress();
    }

    /**
     * Getter for the mockLocalHost for comparison in our tests.
     */
    public InetAddress getMockLocalHost()
    {
        return this.mockLocalHost;
    }

    /**
     * Handled in an asynchronous fashion, this method gets
     * a mocked INetAddress.
     *
     * @param inetAddressListeners
     *         A series of InetAddressListeners which will be notified
     *         when the InetAddress is ready.
     * @return The InetAddress of the device.
     */
    @Override
    protected InetAddress doInBackground(InetAddressListener... inetAddressListeners)
    {
        listeners.addAll(Arrays.asList(inetAddressListeners));
        return this.mockLocalHost;
    }
}
