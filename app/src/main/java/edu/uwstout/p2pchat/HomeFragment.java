package edu.uwstout.p2pchat;


import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.uwstout.p2pchat.databinding.FragmentHomeBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    // private variables
    private FragmentHomeBinding binding = null;

    public HomeFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        WifiP2pDevice thisDevice;
        try
        {
            // Android Studio might complain that getThisDevice might return a nullPointerException,
            // this try catch block will handle that error, so we can safely ignore the warning.
            thisDevice = ((WifiDirectActivity)this.getActivity()).getThisDevice();
        }
        catch (NullPointerException npe) {
            thisDevice = null;
        }
        if (thisDevice != null && ((WifiDirectActivity)this.getActivity()).isP2pEnabled()) {
            this.binding.homeMyDeviceInfo.setText(
                    WifiDirectActivity.summarizeP2pDevice(thisDevice));
        }
        else {
            this.binding.homeMyDeviceInfo.setText(getString(R.string.p2p_not_enabled_error));
        }

        return binding.getRoot();
    }

}
