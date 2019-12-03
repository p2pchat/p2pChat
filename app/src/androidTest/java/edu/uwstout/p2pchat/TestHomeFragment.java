package edu.uwstout.p2pchat;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;

import edu.uwstout.p2pchat.testing.MockViewModel;
import edu.uwstout.p2pchat.testing.MockWifiDirect;

public class TestHomeFragment extends HomeFragment {
    /**
     * Get instance of WifiDirect. Overridable for testing.
     *
     * @param context
     * @return
     */
    @Override
    WifiDirectInterface getWifiDirect(@NonNull Context context) {
        return new MockWifiDirect();
    }

    @Override
    ViewModel getViewModel() {
        return new MockViewModel(null);
    }

    @Override
    public void addMenuItem(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Do nothing
    }
}
