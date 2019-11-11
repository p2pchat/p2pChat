package edu.uwstout.p2pchat;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestNickNameFragment extends NickNameFragment {
    @Override
    public NickNameModal getNickNameModal(Fragment fragment, ArrayAdapter arrayAdapter, List<Peer> peers, int index) {
        return new TestNickNameModal(fragment, arrayAdapter, peers, index);
    }

    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}
