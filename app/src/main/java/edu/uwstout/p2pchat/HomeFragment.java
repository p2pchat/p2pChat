package edu.uwstout.p2pchat;


import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import java.util.List;

import edu.uwstout.p2pchat.databinding.FragmentHomeBinding;
import edu.uwstout.p2pchat.room.Peer;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
        implements WifiDirect.PeerDiscoveryListener
{
    // private variables
    private FragmentHomeBinding binding = null;
    private LiveData<List<Peer>> liveData = null;

    /**
     * The constructor for the class, it is required by java, but is empty
     */
    public HomeFragment()
    {
        // Required empty public constructor
    }


    /**
     * Inflates the fragment view and instantiates essential components of the view for first
     * time setup.
     *
     * @param inflater
     *         a LayoutInflater which handles creating the layout
     *         from the XML file
     * @param container
     *         a ViewGroup that the fragment is contained in
     * @param savedInstanceState
     *         a Bundle created by the OS which may contain information from
     *         last known state. It provides lifecycle functionality.
     * @return the root of the FragmentHomeBinding object used for data binding.
     */
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        WifiP2pDevice thisDevice;
        try
        {
            // Android Studio might complain that getThisDevice might return a nullPointerException,
            // this try catch block will handle that error, so we can safely ignore the warning.
            thisDevice = WifiDirect
                    .getInstance(this.getContext())
                    .getThisDevice();
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
            this.binding
                    .homeMyDeviceInfo
                    .setText(getString(R.string.p2p_not_enabled_error));
        }

        // Have an options menu so that we can programmatically define it in
        // onCreateOptionsMenu
        setHasOptionsMenu(true);

        // subscribe to the events that we need in this context
        WifiDirect
                .getInstance(this.getContext())
                .subscribePeerDiscoveryListener(this);

        liveData = getViewModel().getPeers();

        return binding.getRoot();
    }

    /**
     * creates an options menu so that we have buttons in the toolbar.
     *
     * @param menu
     *         the Menu that we programmatically create
     * @param inflater
     *         a MenuInflater which creates the layout from the XML file
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.add(Menu.NONE, R.id.refresh_peers, Menu.NONE,
                R.string.discover_peers);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    /**
     * handles the event when an item in our options menu is clicked
     *
     * @param item
     *         the MenuItem that was clicked
     * @return a boolean indicating if the click was handled or not
     */
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

    /**
     * Required by the WifiDirect.PeerDiscoveryListener, this method updates the view
     * to display all of the available peers for WifiDirect communication
     *
     * @param wifiP2pDeviceList
     *         the list of peers that are available.
     */
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
        Log.i("HomeFragment", "Size of peer list: " + peers.length);
        // Clear the old peer list
        binding.unrecognizedList.removeAllViews();
        // create a new peer list
        int idTracker = 0;
        final Context thisContext = this.getContext();

        for (WifiP2pDevice device : peers)
        {
            // create a TextView for the WiFiP2p device
            TextView label = new TextView(this.getContext());
            label.setText(WifiDirect.summarizeP2pDevice(device));
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

    /**
     * Required by the WifiDirect.PeerDiscoveryListener interface, this method informs the user
     * that peer discovery failed for some reason.
     *
     * @param reasonCode
     *         the reason for failure
     */
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
                        "Peer discovery failed because Peer to Peer connections are not supported"
                                + " on this device.";
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

    /**
     * Required by the WifiDirect.PeerDiscoveryListener interface, this method notifies
     * the user that we successfully connected to a peer.
     */
    @Override
    public void peerConnectionSucceeded(WifiP2pDevice device)
    {
        Toast.makeText(this.getContext(), "Connection succeeded", Toast.LENGTH_LONG).show();
        ViewModel viewModel = new ViewModel(getActivity().getApplication());

       liveData.observeForever(new Observer<List<Peer>>()
       {
           @Override
           public void onChanged(List<Peer> peers)
           {
              if(!peerExists(peers,device.deviceAddress))
              {
                  viewModel.insertPeer(device.deviceAddress,null);
              }
               HomeFragmentDirections.ChatAction action = HomeFragmentDirections.chatAction();

               action.setAddress(device.deviceAddress);

               Navigation.findNavController(binding.homeMyDeviceTitle)
                       .navigate(action);

           }
       });


    }

    /**
     * Required by the WifiDirect.PeerDiscoveryListener interface, this method notifies
     * * the user that we were unable to connect to a peer.
     *
     * @param reasonCode
     *         the reason for failure (list of reasons unknown at this time)
     */
    @Override
    public void peerConnectionFailed(int reasonCode)
    {
        Toast.makeText(this.getContext(), "Connection failed. Reason: " + reasonCode,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Lifecycle function, this method will ensure that our WifiDirect singleton
     * is properly receiving signals after loosing and regaining focus
     */
    @Override
    public void onResume()
    {
        super.onResume();
        WifiDirect.getInstance(this.getContext()).resume();
    }

    /**
     * Interrupts the WifiDirect singleton from receiving signals while the application
     * has lost focus
     */
    @Override
    public void onPause()
    {
        super.onPause();
        WifiDirect.getInstance(this.getContext()).pause();
    }

    /**
     * Unregisters this fragment as an observer to ensure that we don't have a memory leak
     * when the fragment is destroyed.
     */
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // this is necessary so that we don't have a memory leak
        // caused by old references to old listeners / views that
        // won't exist anymore.
        WifiDirect.getInstance(this.getContext()).unsubscribePeerDiscoveryListener(this);
    }

    /**
     * Returns the View Model.
     * @return View Model
     */
    private ViewModel getViewModel()
    {
        ViewModel viewModel = new ViewModel(getActivity().getApplication());
        return viewModel;
    }

    /**
     * Returns a boolean for peer existence.
     * @param peers a list of peers
     * @param address macAddress of peers
     * @return boolean
     */
    private boolean peerExists(List<Peer> peers, String address)
    {
        for(Peer peer:peers)
        {
            if(peer.macAddress.equals(address))
            {
                return true;
            }
        }
        return false;
    }
}
