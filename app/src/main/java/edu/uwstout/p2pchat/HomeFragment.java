package edu.uwstout.p2pchat;


import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.uwstout.p2pchat.databinding.FragmentHomeBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements WifiDirect.PeerDiscoveryListener
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
            thisDevice = WifiDirect.getInstance(this.getContext()).getThisDevice();
        }
        catch (Exception e)
        {
            thisDevice = null;
        }
        if (thisDevice != null) // problem, this will be null, but if you wait a second it won't be.
        { // we don't want to block the thread, but we need to update when the information is
            // available.
            this.binding.homeMyDeviceInfo.setText(
                    WifiDirect.summarizeP2pDevice(thisDevice));
        }
        else
        {
            this.binding.homeMyDeviceInfo.setText(getString(R.string.p2p_not_enabled_error));
        }

        // Have an options menu so that we can programmatically define it in
        // onCreateOptionsMenu
        setHasOptionsMenu(true);

        // subscribe to the events that we need in this context
        WifiDirect.getInstance(this.getContext()).subscribePeerDiscoveryListener(this);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.add(Menu.NONE, R.id.refresh_peers, Menu.NONE,
                R.string.discover_peers);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.refresh_peers)
        {
            WifiDirect.getInstance(this.getContext()).discoverPeers();
            return true;
        }
        else
        {
            Log.e("HomeFragment", "Unrecognized options item: " + item.getItemId());
            return false;
        }

    }

    @Override
    public void peerListChanged(WifiP2pDeviceList wifiP2pDeviceList)
    {
        /*
            update the lists in the GUI, this will be a complicated process
            because we will have to access the model, find out if any of the
            peers are previous connections, and then sort them into their
            corresponding lists. This however, doesn't work that well when you
            consider that the model won't be ready until the next sprint.

            For now, since there is no model, there is no "recognized" peers.
            So we will just put all peers in the unrecognized list.
         */
        // TODO refactor this method to incorporate the model when it's ready!
        // Parse the device list and put their details as textViews
        final WifiP2pDevice[] peers =
                wifiP2pDeviceList.getDeviceList().toArray(new WifiP2pDevice[0]);

        // Log some information for sanity check
        Log.i("MainActivity", "Size of peer list: " + peers.length);
        // Clear the old peer list
        binding.unrecognizedList.removeAllViews();
        // create a new peer list
        int idTracker = 0;
        final Context thisContext = this.getContext();

        for (WifiP2pDevice device : peers)
        {
            // create a TextView for the WiFiP2p device
            TextView label = new TextView(this.getContext());
            label.setText(device.toString());
            label.setId(idTracker++);
            label.setClickable(true);
            label.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            // add an onClickListener
            label.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                { // I need a way to ensure the context isn't null?
                    WifiDirect.getInstance(thisContext).connectToDevice(peers[view.getId()]);
                }
            });
            // add the TextView to the view
            binding.unrecognizedList.addView(label);
        }
    }

    @Override
    public void peerDiscoveryFailed(int reasonCode)
    {
        // Turn the error code into a human readable message.
        // Other error handling may occur in this code.
        String errorType;
        switch (reasonCode)
        {
            case WifiP2pManager.ERROR:
                errorType = "Peer discovery failed due to an internal error.";
                break;
            case WifiP2pManager.P2P_UNSUPPORTED:
                errorType =
                        "Peer discovery failed because Peer to Peer connections are not supported" +
                                " on this device.";
                break;
            case WifiP2pManager.BUSY:
                errorType =
                        "Peer discovery failed because the framework is busy and is unable to " +
                                "service the request.";
                break;
            case WifiP2pManager.NO_SERVICE_REQUESTS:
                errorType = "Peer discovery failed because no service channel requests were added.";
                break;
            default:
                errorType =
                        "Peer discovery failed due to an unknown error. Reason Code: " + reasonCode;
                break;
        }
        // Make a toast telling the user what went wrong
        Toast.makeText(this.getContext(), errorType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        WifiDirect.getInstance(this.getContext()).resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        WifiDirect.getInstance(this.getContext()).pause();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // this is necessary so that we don't have a memory leak
        // caused by old references to old listeners / views that
        // won't exist anymore.
        WifiDirect.getInstance(this.getContext()).unsubscribePeerDiscoveryListener(this);
    }
}
