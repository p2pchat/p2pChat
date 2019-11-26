package edu.uwstout.p2pchat;

import static edu.uwstout.p2pchat.testing.MockSendDataService.awaitIntent;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;

import androidx.lifecycle.LiveData;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import edu.uwstout.p2pchat.WifiDirectHelpers.LocalhostAsyncTask;
import edu.uwstout.p2pchat.WifiDirectHelpers.LocalhostAsyncTaskFactory;
import edu.uwstout.p2pchat.WifiDirectHelpers.ReceiverAsyncTask;
import edu.uwstout.p2pchat.WifiDirectHelpers.ReceiverAsyncTaskFactory;
import edu.uwstout.p2pchat.WifiDirectHelpers.SendDataService;
import edu.uwstout.p2pchat.WifiDirectHelpers.UpdaterAsyncTask;
import edu.uwstout.p2pchat.WifiDirectHelpers.UpdaterAsyncTaskFactory;
import edu.uwstout.p2pchat.room.LiveDataPromise;
import edu.uwstout.p2pchat.room.Message;
import edu.uwstout.p2pchat.testing.MockLocalhostAsyncTask;
import edu.uwstout.p2pchat.testing.MockReceiverAsyncTask;
import edu.uwstout.p2pchat.testing.MockSendDataService;
import edu.uwstout.p2pchat.testing.MockUpdaterAsyncTask;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Instrumented testing of the WifiDirect Singleton
 */
@RunWith(AndroidJUnit4.class)
public class WifiDirectInstrumentedTest
{
    /**
     * Enables accessing the singleton on-demand.
     */
    private Context testContext;
    /**
     * Class reference to the singleton that we are testing.
     */
    private WifiDirect instance;
    /**
     * Mock dependency for the Database ViewModel
     */
    private MockViewModel mockViewModel;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new
            ActivityTestRule<>(MainActivity.class);

    private <T> T await(LiveData<T> liveData) {
        LiveDataPromise<T> liveDataPromise = new LiveDataPromise<>(liveData);
        return liveDataPromise.await();
    }

    // Create a mock WifiP2pGroup class for testing.
    class MockGroup extends WifiP2pGroup {
        private WifiP2pDevice mockDevice;
        private boolean isGroupOwner;

        /**
         * Creates a mock WifiP2pGroup which returns variables
         * that this mock is instantiated with.
         * @param mockPeer The WifiP2pDevice that getOwner() should return and will be the only
         *                 value in the collection returned by getClientList().
         * @param isGroupOwner Whether this device is the host / owner of the fake group.
         */
        MockGroup(WifiP2pDevice mockPeer, boolean isGroupOwner) {
            this.mockDevice = mockPeer;
            this.isGroupOwner = isGroupOwner;
        }
        @Override
        public Collection<WifiP2pDevice> getClientList()
        {
            ArrayList<WifiP2pDevice> mockList = new ArrayList<>();
            mockList.add(this.mockDevice);
            return mockList;
        }

        @Override
        public boolean isGroupOwner()
        {
            return this.isGroupOwner;
        }

        @Override
        public WifiP2pDevice getOwner()
        {
            return this.mockDevice;
        }
    }

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

        // substitute ALL dependencies with Mocks using a modified factory pattern
        // alongside the dependency injection pattern.
        this.instance.setSenderService(MockSendDataService.class);
        this.instance.setMessageReceiver(new ReceiverAsyncTaskFactory()
        {
            @Override
            public ReceiverAsyncTask newReceiverTask()
            {
                // Default to one method, but some tests may want to make
                // this zero or more than one message.
                return new MockReceiverAsyncTask(1);
            }
        });
        this.instance.setLocalHostResolver(new LocalhostAsyncTaskFactory()
        {
            @Override
            public LocalhostAsyncTask newLocalhostTask()
            {
                return new MockLocalhostAsyncTask();
            }
        });
        this.instance.setClientUpdateReceiver(new UpdaterAsyncTaskFactory()
        {
            @Override
            public UpdaterAsyncTask newUpdateTask()
            {
                return new MockUpdaterAsyncTask();
            }
        });
        this.mockViewModel = new MockViewModel(null);
        this.instance.setViewModel(this.mockViewModel);
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
        final WifiP2pDevice TEST_DEVICE = new WifiP2pDevice();
        TEST_DEVICE.deviceAddress = "TEST_STRING";

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
                assert(device.deviceAddress.equals(TEST_DEVICE.deviceAddress));
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
        instance.onGroupInfoAvailable(new MockGroup(TEST_DEVICE, true));
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
        // This test shouldn't "receive" any messages for the database.
        this.instance.setMessageReceiver(new ReceiverAsyncTaskFactory()
        {
            @Override
            public ReceiverAsyncTask newReceiverTask()
            {
                return new MockReceiverAsyncTask(0);
            }
        });
        final String TEST_STRING = "This is a test";
        // Use some mock information to get us going.
        WifiP2pInfo info = new WifiP2pInfo();
        info.groupFormed = true;
        info.isGroupOwner = true;
        try
        {
            info.groupOwnerAddress = InetAddress.getByName("127.0.0.1");
        }
        catch (UnknownHostException e)
        {
            assert false;
            return;
        }
        // Has to be called before WifiDirect.onConnectionInfoAvailable()
        this.instance.onGroupInfoAvailable(new MockGroup(new WifiP2pDevice(), false));
        // Has to be called before WifiDirect.sendInMemoryFile();
        this.instance.onConnectionInfoAvailable(info);
        // Create a dummy InMemoryFile for testing.
        InMemoryFile testMessage = new InMemoryFile(TEST_STRING);
        // Send the message, which should use our mock service
        instance.sendInMemoryFile(testMessage);
        Intent actualIntent = awaitIntent();
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
    public void clientSetsUpAndSendsCorrectly()
    {
        // Step 1, dependency injection for client devices. Handled in setup function.
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
        // onGroupInfoAvailable must be called before onConnectionInfoAvailable
        this.instance.onGroupInfoAvailable(new MockGroup(new WifiP2pDevice(), false));
        this.instance.onConnectionInfoAvailable(info);
        // Step 3, Check that the client sends information about how to contact itself
        // to the host via our MockSendDataService
        Intent updateIntent = MockSendDataService.awaitIntent();
        // We want to check both that the target IP address is the one we mocked,
        // AND that the intent is packaged correctly, because this is occurring outside
        // of sendInMemoryFile, which is what the other tests check.
        if (updateIntent != null && updateIntent.getExtras() != null)
        {
            // Check that the client is sending the address to the mock data we gave it.
            String targetAddress =
                    updateIntent.getExtras().getString(SendDataService.EXTRAS_PEER_ADDRESS);
            assert (Objects.requireNonNull(targetAddress)
                    .equals(info.groupOwnerAddress.getHostAddress()));
            // Check that the client has the right action associated with the intent.
            assert(Objects.equals(updateIntent.getAction(),
                    SendDataService.ACTION_UPDATE_INETADDRESS));
            // Check that the client has the right port configured.
            assert(Objects.equals(Objects.requireNonNull(updateIntent.getExtras())
                            .getInt(SendDataService.EXTRAS_PEER_PORT),
                    ReceiverAsyncTask.MAGIC_PORT));
            // extract the InetAddress that is the info wanted to send.
            InetAddress actualAddr =
                    Objects.requireNonNull(updateIntent.getExtras()
                            .getParcelable(SendDataService.EXTRAS_INETADDRESS));
            // check that the InetAddress matches our mock data.
            assert(actualAddr == info.groupOwnerAddress);
        }
        else
        {
            // If we enter this block, something went wrong when awaiting
            // the intent, or the intent wasn't packaged correctly.
            assert false;
        }
        // Step 4, Check that the received text message was saved to the database.
        boolean messageFound = false;
        String targetTextMessage = MockReceiverAsyncTask.getMockFile().getTextMessage();
        for (Message message :
                Objects.requireNonNull(
                        mockViewModel.getMessages(info.groupOwnerAddress.getHostAddress())
                                .getValue()))
        {
            if (!message.isFile() && message.content.equals(targetTextMessage))
            {
                messageFound = true;
            }
        }
        assert(messageFound);
    }

    /**
     * Tests that when this device is the host/server, it can
     * receive information from the client and use it to send
     * data to the client (essential for two way communication).
     */
    @Test
    public void hostSetsUpAndSendsCorrectly()
    {
        // Step 1, dependency injection for client devices handled in setup function.
        // Step 2, create a WifiP2pInfo object where group formed is true,
        // groupOwner is true, and groupOwnerAddress is set to something that we
        // can test against (check) later.
        WifiP2pInfo info = new WifiP2pInfo();
        info.groupFormed = true;
        info.isGroupOwner = true;
        try
        {
            info.groupOwnerAddress = InetAddress.getByName("127.0.0.1");
        }
        catch (UnknownHostException e)
        {
            assert false;
            return;
        }
        WifiP2pDevice device = new WifiP2pDevice();
        device.deviceAddress = info.groupOwnerAddress.getHostAddress();
        this.instance.onGroupInfoAvailable(new MockGroup(device, true));
        this.instance.onConnectionInfoAvailable(info);
        // Step 3, verify that the Host persists data from our mocked client.
        // we know this is true if the host attempts to send data to the
        // IP address we mocked, which is given to our mock SendDataService.
        InMemoryFile mockIMF = new InMemoryFile("let me simply add that it's my very good honor " +
                "to meet you and you may call me V.");
        this.instance.sendInMemoryFile(mockIMF);
        Intent sentIntent = MockSendDataService.awaitIntent();
        /*
            At this point, several things have occurred:
            1. Our host has received the clientAddress from our mock.
            2. Our host has been told to send messages to our mock.
            3. Because onConnectionInfo was called, we can send a message.
            It is also worth noting that we don't need to check the contents
            of the InMemoryFile because that is checked in a different test.
            We only care about one thing: The address it is attempting to send to.
         */
        if (sentIntent != null && sentIntent.getExtras() != null)
        { // unwrapping for the sake of the linter
            String targetAddress =
                    sentIntent.getExtras().getString(SendDataService.EXTRAS_PEER_ADDRESS);
            assert(Objects.requireNonNull(targetAddress)
                    .equals(MockUpdaterAsyncTask.getMockClientAddress().getHostAddress()));
        }
        else
        {
            // If we enter this block, something went wrong when awaiting
            // the intent, or the intent wasn't packaged correctly.
            assert false;
        }
        // Step 4, check if the database has been updated with the fake message we mocked.
        // Specifically check messages that we received, since we can't check the one we sent.
        boolean messageFound = false;
        String targetTextMessage = MockReceiverAsyncTask.getMockFile().getTextMessage();
        List<Message> messageList =
                await(mockViewModel.getMessages(info.groupOwnerAddress.getHostAddress()));
        for (Message message : messageList)
        {
            if (!message.isFile() && message.content.equals(targetTextMessage))
            {
                messageFound = true;
            }
        }
        assert(messageFound);
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
