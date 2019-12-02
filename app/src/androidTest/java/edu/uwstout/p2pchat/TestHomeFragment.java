package edu.uwstout.p2pchat;

import android.content.Context;

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
}
