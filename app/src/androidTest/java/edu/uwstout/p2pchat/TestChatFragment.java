package edu.uwstout.p2pchat;

import android.app.Application;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Tests version of ChatFragment that uses MockViewModel for testing
 */
public class TestChatFragment extends ChatFragment {
    /**
     * Returns a MockViewModel
     * @param app Current application
     * @return
     */
    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }

    /**
     * Returns a default mac address
     * @return
     */
    @Override
    public String getPeerMacAddress() {
        return MockPeers.austin.macAddress;
    }
}
