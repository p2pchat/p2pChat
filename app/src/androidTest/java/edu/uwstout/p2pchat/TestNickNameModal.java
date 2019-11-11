package edu.uwstout.p2pchat;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestNickNameModal extends NickNameModal {
    public TestNickNameModal(Fragment fragment, ArrayAdapter adapter, List<Peer> peers, int index) {
        super(fragment, adapter, peers, index);
    }

    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}
