package edu.uwstout.p2pchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import edu.uwstout.p2pchat.FileTransfer.ReceiverAsyncTask;
import edu.uwstout.p2pchat.FileTransfer.SendDataService;
import edu.uwstout.p2pchat.testing.MockSendDataService;

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
     * can call the function WifiDirect.getInstance(),
     * then do setup for dependency injection on the
     * WifiDirect singleton.
     */
    @Before
    public void setup()
    {
        // get context so that we can call WifiDirect.getInstance()
        this.testContext = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        // Get an instance so that we don't have repetitive code.
        WifiDirect testDirect = WifiDirect.getInstance(this.testContext);
        // Disable other listeners so that we don't call things we don't want to call.
        testDirect.clearListenerLists();
    }

    /**
     * Resets the WifiDirectSingleton between tests.
     */
    @After
    public void tearDown()
    {
        WifiDirect testDirect = WifiDirect.getInstance(this.testContext);
        // Disable other listeners so that we don't call things we don't want to call.
        testDirect.resetSenderService();
    }

    /**
     * Test all the getters and setters that we can
     * see the output of externally.
     */
    @Test
    public void gettersAndSetters()
    {
        // Get an instance so that we aren't repeating the same code.
        WifiDirect testDirect = WifiDirect.getInstance(this.testContext);
        // Test the isP2pEnabled getters and setters
        testDirect.setP2pEnabled(true);
        assert(testDirect.isP2pEnabled());

        WifiP2pDevice mockDevice = new WifiP2pDevice();
        mockDevice.deviceAddress = "test string";

        // Test the thisDevice getters and setters
        testDirect.setThisDevice(mockDevice);
        WifiP2pDevice retrievedDevice = testDirect.getThisDevice();

        assert(retrievedDevice != null && retrievedDevice.deviceAddress
                .equals(mockDevice.deviceAddress));

        // since we haven't attempted to connect to a device,
        // peerDevice should be null
        assert(testDirect.getPeerDevice() == null);
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
                assert(callCount == 0);
                callCount++;
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
                assert(reasonCode == REASON_CODE_TEST);
            }
        }
        // Let's begin the actual testing
        // First, create a PDL instance
        PDL pdl = new PDL();
        // second, subscribe our listener.
        WifiDirect instance = WifiDirect.getInstance(this.testContext);
        instance.subscribePeerDiscoveryListener(pdl);
        // third, call some of the functions that we can test
        instance.peersHaveChanged(new WifiP2pDeviceList());
        instance.peerDiscoveryFailed(REASON_CODE_TEST);
        // fourth, unsubscribe
        instance.unsubscribePeerDiscoveryListener(pdl);
        // fifth, call the function again, if the PDL
        // function is called again, it will assert false.
        instance.peersHaveChanged(new WifiP2pDeviceList());
    }

    /**
     * Tests that data sent to the SendDataService is packaged
     * correctly inside the intent which triggers the service.
     * Uses dependency injection to mock the SendDataService.
     */
    @Test
    public void sendIntentPackagedCorrectly()
    {
        final String TEST_STRING = "This is a test";
        // Get an instance of the singleton
        WifiDirect instance = WifiDirect.getInstance(this.testContext);
        // Change the sender service to the mock
        instance.setSenderService(MockSendDataService.class);
        // Create a dummy InMemoryFile for testing.
        InMemoryFile testMessage = new InMemoryFile(TEST_STRING);
        // Send the message, which should use our mock service
        instance.sendInMemoryFile(testMessage);
        // This may create a race condition!!!
        Intent actualIntent = MockSendDataService.await();
        // Make assertions based on if the intent for sending
        // has the proper contents.
        if (actualIntent != null)
        {
            assert(Objects.equals(actualIntent.getAction(),
                    SendDataService.ACTION_SEND_DATA));
            assert(Objects.equals(Objects.requireNonNull(actualIntent.getExtras())
                            .getInt(SendDataService.EXTRAS_PEER_PORT),
                    ReceiverAsyncTask.MAGIC_PORT));
            // Extract the InMemoryFile
            InMemoryFile actualData =
                    Objects.requireNonNull(actualIntent.getExtras()
                            .getParcelable(SendDataService.EXTRAS_IN_MEMORY_FILE));
            // Check that the InMemoryFile has the proper contents.
            assert(actualData.getMimeType().equals(InMemoryFile.MESSAGE_MIME_TYPE));
            assert(Objects.equals(actualData.getTextMessage(), TEST_STRING));
        }
        else
        {
            // A race condition or error occurred when
            // awaiting the intent.
            assert(false);
        }
    }

    /*
        The following list of functions are WifiDirect functions
        which cannot be tested without two devices and an
        actual connection. Testing these will have to be done
        by hand.
        Untested functions:
        connectToDevice
        onConnectionInfoAvailable
        onChannelDisconnected
        discoverPeers
     */

}
