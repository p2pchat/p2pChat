package edu.uwstout.p2pchat.WifiDirectHelpers;

import java.net.InetAddress;

public interface InetAddressListener
{
    /**
     * Notifies the listener that the INetAddress is available.
     */
    public void onLocalHostAvailable(InetAddress address);
}
