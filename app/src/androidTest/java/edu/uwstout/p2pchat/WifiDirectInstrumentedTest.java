package edu.uwstout.p2pchat;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented testing of the WifiDirect Singleton
 */
@RunWith(AndroidJUnit4.class)
public class WifiDirectInstrumentedTest
{
    private Context testContext;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new
            ActivityTestRule<>(MainActivity.class);

    /**
     * Instantiate the test context variable so that we
     * can call the function WifiDirect.getInstance().
     */
    @Before
    public void setup()
    {
        // get context so that we can call WifiDirect.getInstance()
        this.testContext = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
    }

    /**
     * Test all the getters and setters that we can
     * see the output of externally.
     */
    @Test
    public void gettersAndSetters()
    {
        // Test the isP2pEnabled getters and setters
        WifiDirect.getInstance(this.testContext)
                .setP2pEnabled(true);
        assert(WifiDirect.getInstance(this.testContext).isP2pEnabled());

        WifiP2pDevice mockDevice = new WifiP2pDevice();
        mockDevice.deviceAddress = "test string";

        // Test the thisDevice getters and setters
        WifiDirect.getInstance(this.testContext).setThisDevice(mockDevice);
        WifiP2pDevice retrievedDevice = WifiDirect.getInstance(this.testContext).getThisDevice();

        assert(retrievedDevice != null && retrievedDevice.deviceAddress
                .equals(mockDevice.deviceAddress));

        // since we haven't attempted to connect to a device,
        // peerDevice should be null
        assert(WifiDirect.getInstance(this.testContext)
            .getPeerDevice() == null);
    }

    /**
     * Tests that the observer pattern is successfully
     * linked up, and that listeners can unsubscribe.
     */
    @Test
    public void peerDiscoveryListenersSubscriptionStatus()
    {
        final int REASON_CODE_TEST = 1;
        final WifiP2pDevice DEVICE_TEST = new WifiP2pDevice();
        DEVICE_TEST.deviceAddress = "TEST_STRING";

        // Create a mock peerDiscoveryListener that can
        // make assert statements when we want.
        class PDL implements WifiDirect.PeerDiscoveryListener
        {
            private int callCount = 0;
            public void peerListChanged(WifiP2pDeviceList wifiP2pDeviceList)
            {
                assert(callCount++ == 0);
            }
            public void peerDiscoveryFailed(int reasonCode)
            {
                assert(reasonCode == REASON_CODE_TEST);
            }
            public void peerConnectionSucceeded(WifiP2pDevice device)
            {
                assert(device.deviceAddress.equals(DEVICE_TEST.deviceAddress));
            }

            public void peerConnectionFailed(int reasonCode)
            {
                // we will be calling this function after
                // unsubscribing, so if this is called,
                // mistakes were made.
                assert(false);
            }
        }
        // Let's begin the actual testing
        // First, create a PDL instance
        PDL pdl = new PDL();
        // second, subscribe our listener.
        WifiDirect instance = WifiDirect.getInstance(this.testContext);
        instance.subscribePeerDiscoveryListener(pdl);
        // third, call one of the functions that we can test
        instance.peersHaveChanged(new WifiP2pDeviceList());
        // fourth, unsubscribe
        instance.unsubscribePeerDiscoveryListener(pdl);
        // fifth, call the function again, if the PDL
        // function is called again, it will assert false.
        instance.peersHaveChanged(new WifiP2pDeviceList());

        // throws android.view.ViewRootImpl$CalledFromWrongThreadException
    }

}
