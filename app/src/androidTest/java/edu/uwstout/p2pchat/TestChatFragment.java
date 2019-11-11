package edu.uwstout.p2pchat;

import android.app.Application;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestChatFragment extends ChatFragment {
    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }

    @Override
    public String getPeerMacAddress() {
        return MockPeers.austin.macAddress;
    }
}
