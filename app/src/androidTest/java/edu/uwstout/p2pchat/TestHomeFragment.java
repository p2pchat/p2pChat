package edu.uwstout.p2pchat;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;

import edu.uwstout.p2pchat.testing.MockViewModel;
import edu.uwstout.p2pchat.testing.MockWifiDirect;

public class TestHomeFragment extends HomeFragment {

    static boolean navigated = false;
    static WifiP2pDevice navigationDevice = null;
    public static void reset() {
        navigated = false;
        navigationDevice = null;
    }
    public static boolean getNavigated() {
        return navigated;
    }
    public static WifiP2pDevice getNavigationDevice() {
        return navigationDevice;
    }

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

    /**
     * Overridable for testing
     *
     * @param device
     */
    @Override
    public void navigateToChatFragment(WifiP2pDevice device) {
        navigated = true;
        navigationDevice = device;
    }
}
