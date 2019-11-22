package edu.uwstout.p2pchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import edu.uwstout.p2pchat.WifiDirectHelpers.ReceiverAsyncTask;
import edu.uwstout.p2pchat.WifiDirectHelpers.SendDataService;
import edu.uwstout.p2pchat.testing.MockLocalHostHelper;
import edu.uwstout.p2pchat.testing.MockReceiverAsyncTask;
import edu.uwstout.p2pchat.testing.MockSendDataService;
import edu.uwstout.p2pchat.testing.MockUpdaterAsyncTask;

/**
 * Instrumented testing of the WifiDirect Singleton
 */
@RunWith(AndroidJUnit4.class)
public class WifiDirectInstrumentedTest
{
    private Context testContext;
    private WifiDirect instance;

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
        // Get a class instance so that we don't have repetitive code in multiple functions.
        this.instance = WifiDirect.getInstance(this.testContext);
        // Disable other listeners so that we don't call things we don't want to call.
        this.instance.clearListenerLists();
    }

    /**
     * Resets the WifiDirectSingleton between tests.
     */
    @After
    public void tearDown()
    {
        this.instance.resetDependencies();
        // remove any listeners added from the tests.
        this.instance.clearListenerLists();
    }

    /**
     * Test all the getters and setters that we can
     * see the output of externally.
     */
    @Test
    public void gettersAndSetters()
    {
        // Test the isP2pEnabled getters and setters
        this.instance.setP2pEnabled(true);
        assert(this.instance.isP2pEnabled());

        WifiP2pDevice mockDevice = new WifiP2pDevice();
        mockDevice.deviceAddress = "test string";

        // Test the thisDevice getters and setters
        this.instance.setThisDevice(mockDevice);
        WifiP2pDevice retrievedDevice = this.instance.getThisDevice();

        assert(retrievedDevice != null && retrievedDevice.deviceAddress
                .equals(mockDevice.deviceAddress));

        // since we haven't attempted to connect to a device,
        // peerDevice should be null
        assert(this.instance.getPeerDevice() == null);
    }

    /**
     * Tests that the observer pattern is successfully
     * linked up, and that peer discovery listeners can unsubscribe.
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
     * Tests that the observer pattern is successfully
     * linked up, and that disconnection listeners can unsubscribe.
     */
    @Test
    public void disconnectionListenersSubscriptionStatus()
    {
        WifiDirect.DisconnectionListener dl = new WifiDirect.DisconnectionListener()
        {
            private int callCount = 0;

            /**
             * Will assert true the first time it is called,
             * and false every time after that.
             */
            @Override
            public void onPeerDisconnect()
            {
                if (callCount++ >= 1)
                {
                    assert(false);
                }
                else
                {
                    assert(true);
                }
            }
        };
        // Check that in the next three lines of code, we call onPeerDisconnect, only once.
        // shouldn't call our listener because we haven't subscribed yet.
        this.instance.onChannelDisconnected();
        // subscribe our listener
        this.instance.subscribeDisconnectionListener(dl);
        // This should be the first and only time we call our listener.
        this.instance.onChannelDisconnected();
        // unsubscribe
        this.instance.unSubscribeDisconnectionListener(dl);
        // This shouldn't call our listener because we unsubscribed.
        this.instance.unSubscribeDisconnectionListener(dl);
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
        Intent actualIntent = MockSendDataService.awaitIntent();
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
            // If we enter this block, an error occurred when
            // awaiting the intent.
            assert(false);
        }
    }

    /**
     * Tests that WifiDirect is using asynchronous tasks to get information to put in the
     * database from peers.
     */
    @Test
    public void savesMockedMessageAsClient()
    {
        // Step 1, dependency injection for client devices.
        this.instance.setSenderService(MockSendDataService.class);
        MockReceiverAsyncTask mockReceiverAsyncTask = new MockReceiverAsyncTask(1);
        this.instance.setMessageReceiver(mockReceiverAsyncTask);
        MockLocalHostHelper mockLocalHostHelper = new MockLocalHostHelper();
        this.instance.setLocalHostResolver(mockLocalHostHelper);
        // Step 2, create a WifiP2pInfo object where group formed is true,
        // isGroupOwner is false, and GroupOwnerAddress is assigned a variable.
        WifiP2pInfo info = new WifiP2pInfo();
        info.groupFormed = true; // We can do some connecting.
        info.isGroupOwner = false; // We are the client in our group.
        try
        {
            info.groupOwnerAddress = InetAddress.getByName("127.0.0.1");
        }
        catch (UnknownHostException e)
        {
            assert false;
            return;
        }
        this.instance.onConnectionInfoAvailable(info);
        // Now, Step 3 is incredibly complex.
        // Make sure that our fake text message was added to the database.
        // Make sure that our MockSendDataService was sent a properly packaged intent
        // that is supposed to inform a host about it's InetAddress (taken from MockLocalHostHelper)
        // Maybe add a thing to the tearDown function that clears the database between tests.
        assert false; // until full implementation is added.
    }

    /*
        The following list of functions are WifiDirect functions
        which cannot be tested without two devices and an
        actual connection. Testing these will have to be done
        by hand.
        Untested functions:
        discoverPeers - Handled entirely with WifiP2pManager
        connectToDevice - Triggers onConnectionInfoAvailable's behavior, but makes a call directly
            to the WifiP2pManager. Which is marked as do not mock.
     */

}
