package edu.uwstout.p2pchat;

import android.app.Application;

import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestNickNameFragment extends NickNameFragment {
    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}
