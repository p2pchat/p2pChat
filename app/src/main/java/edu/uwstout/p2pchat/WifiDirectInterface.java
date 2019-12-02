package edu.uwstout.p2pchat;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Interface implemented by WifiDirect and MockWifiDirect
 */
public interface WifiDirectInterface extends WifiP2pManager.ChannelListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.GroupInfoListener
{
    void setP2pEnabled(final boolean enabled);
    boolean isP2pEnabled();
    WifiP2pDevice getThisDevice();
    void setThisDevice(final WifiP2pDevice device);
    WifiP2pDevice getPeerDevice();
    void subscribePeerDiscoveryListener(final WifiDirect.PeerDiscoveryListener pdl);
    void unsubscribePeerDiscoveryListener(final WifiDirect.PeerDiscoveryListener pdl);
    void subscribeDisconnectionListener(final WifiDirect.DisconnectionListener disconnectionListener);
    void unSubscribeDisconnectionListener(final WifiDirect.DisconnectionListener disconnectionListener);
    void resume();
    void pause();
    void discoverPeers();
    void peersHaveChanged(final WifiP2pDeviceList wifiP2pDeviceList);
    void connectToDevice(final WifiP2pDevice device);
    void sendInMemoryFile(final InMemoryFile inMemoryFile);
}
