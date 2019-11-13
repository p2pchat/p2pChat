package edu.uwstout.p2pchat;


import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit test class for the WifiDirect Singleton
 */
public class WifiDirectTest
{
    /**
     * Testing summarizeP2pDevice where the WifiP2pDevice
     * has a non-null value in its variable
     * "secondaryDeviceType"
     */
    @Test
    public void deviceSummaryWithSecondaryDeviceType() {
        // create a mock WifiP2pDevice
        WifiP2pDevice mock = new WifiP2pDevice();
        mock.deviceName = "MockDevice";
        mock.deviceAddress = "123 Mock Lane";
        mock.primaryDeviceType = "Mocking you";
        mock.secondaryDeviceType = "Mocking itself";

        String expected = mock.deviceName + "\n"
                + mock.deviceAddress + "\n"
                + mock.primaryDeviceType + "\n"
                + mock.secondaryDeviceType;

        String actual = WifiDirect.summarizeP2pDevice(mock);

        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Testing summarizeP2pDevice where the WifiP2pDevice
     * has a null value in the variable "secondaryDeviceType"
     */
    @Test
    public void deviceSummaryWithoutSecondaryDeviceType() {
        // create a mock WifiP2pDevice
        WifiP2pDevice mock = new WifiP2pDevice();
        mock.deviceName = "MockDevice";
        mock.deviceAddress = "123 Mock Lane";
        mock.primaryDeviceType = "Mocking you";

        String expected = mock.deviceName + "\n"
                + mock.deviceAddress + "\n"
                + mock.primaryDeviceType + "\n";

        String actual = WifiDirect.summarizeP2pDevice(mock);

        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(expected);
    }


}
