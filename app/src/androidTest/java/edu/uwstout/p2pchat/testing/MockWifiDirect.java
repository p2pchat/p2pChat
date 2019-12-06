package edu.uwstout.p2pchat.testing;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.InMemoryFile;
import edu.uwstout.p2pchat.WifiDirect;

public class MockWifiDirect extends WifiDirect {
    static List<WifiDirect.PeerDiscoveryListener> peerDiscoveryListeners = new ArrayList<>();
    static List<WifiDirect.DisconnectionListener> disconnectionListeners = new ArrayList<>();

    static boolean p2pEnabled = false;
    static WifiP2pDevice thisDevice = null;
    static List<WifiP2pDevice> peerDevices = new ArrayList<>();
    static int peerConnectedTo = -1;

    public static void reset() {
        peerDiscoveryListeners.clear();
        disconnectionListeners.clear();

        p2pEnabled = false;
        thisDevice = null;
        peerDevices.clear();
        peerConnectedTo = -1;
    }

    public static void setPeerDevices(List<WifiP2pDevice> devices) {
        peerDevices = devices;
    }


    @Override
    public void setP2pEnabled(boolean enabled) {
        p2pEnabled = enabled;
    }

    @Override
    public boolean isP2pEnabled() {
        return p2pEnabled;
    }

    @Override
    public WifiP2pDevice getThisDevice() {
        return thisDevice;
    }

    @Override
    public void setThisDevice(WifiP2pDevice device) {
        thisDevice = device;
    }

    @Override
    public WifiP2pDevice getPeerDevice() {
        WifiP2pDevice device = new WifiP2pDevice();
        device.deviceName = "Austin's device";
        device.primaryDeviceType = "Mock device";
        device.deviceAddress = MockPeers.austin.macAddress;
        return device;
    }

    @Override
    public void subscribePeerDiscoveryListener(WifiDirect.PeerDiscoveryListener pdl) {
        peerDiscoveryListeners.add(pdl);
    }

    @Override
    public void unsubscribePeerDiscoveryListener(WifiDirect.PeerDiscoveryListener pdl) {
        peerDiscoveryListeners.remove(pdl);
    }

    @Override
    public void subscribeDisconnectionListener(WifiDirect.DisconnectionListener disconnectionListener) {
        disconnectionListeners.add(disconnectionListener);
    }

    @Override
    public void unSubscribeDisconnectionListener(WifiDirect.DisconnectionListener disconnectionListener) {
        disconnectionListeners.remove(disconnectionListener);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public MockWifiDirect(@NonNull Context c)
    {
        super(c);
    }

    @Override
    public void discoverPeers() {

        WifiP2pDeviceList deviceList = new WifiP2pDeviceList() {
            @Override
            public WifiP2pDevice get(String deviceAddress) {
                for(WifiP2pDevice device : peerDevices) {
                    if(device.deviceAddress.equals(deviceAddress)) {
                        return device;
                    }
                }
                return null;
            }

            @Override
            public Collection<WifiP2pDevice> getDeviceList() {
                return peerDevices;
            }
        };
        peersHaveChanged(deviceList);
    }

    @Override
    public void peersHaveChanged(WifiP2pDeviceList wifiP2pDeviceList) {
        for (WifiDirect.PeerDiscoveryListener pdl : peerDiscoveryListeners) {
            pdl.peerListChanged(wifiP2pDeviceList);
        }
    }

    @Override
    public void connectToDevice(WifiP2pDevice device) {
        for(int i = 0;i<peerDevices.size();i++) {
            if(peerDevices.get(i).deviceAddress.equals(device.deviceAddress)) {
                peerConnectedTo = i;
                break;
            }
        }
        for (WifiDirect.PeerDiscoveryListener pdl : peerDiscoveryListeners) {
            pdl.peerConnectionSucceeded(device);
        }
    }

    @Override
    public void sendInMemoryFile(InMemoryFile inMemoryFile) {
        MockViewModel mockViewModel = new MockViewModel(null);
        mockViewModel.insertMessage(peerDevices.get(peerConnectedTo).deviceAddress, new Date(), true, inMemoryFile.getMimeType(), inMemoryFile.getTextMessage());
    }

    @Override
    public void onChannelDisconnected() {
        for (WifiDirect.DisconnectionListener disconnectionListener : disconnectionListeners) {
            disconnectionListener.onPeerDisconnect();
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    @Override
    public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {

    }
}
