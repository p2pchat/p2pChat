package edu.uwstout.p2pchat;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Creates a Test version of the NickNameModal that uses a MockViewModel
 */
public class TestNickNameModal extends NickNameModal {
    /**
     * Pass through constructor
     * @param fragment
     * @param adapter
     * @param peers
     * @param index
     */
    public TestNickNameModal(Fragment fragment, ArrayAdapter adapter, List<Peer> peers, int index) {
        super(fragment, adapter, peers, index);
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
