package edu.uwstout.p2pchat;


import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Test version of NickNameFramgment that uses a MockViewModel for testing
 */
public class TestNickNameFragment extends NickNameFragment {
    /**
     * Returns a TestNickNameModel that uses a MockViewModal
     * @param fragment
     * @param arrayAdapter
     * @param peers
     * @param index
     * @return
     */
    @Override
    public NickNameModal getNickNameModal(String macAddress)
    {
        return new TestNickNameModal(classContext, macAddress);
    }

    /**
     * Returns a MockViewModel
     * @param app
     * @return
     */
    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}