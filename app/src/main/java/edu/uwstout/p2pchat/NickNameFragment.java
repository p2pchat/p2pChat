package edu.uwstout.p2pchat;


import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import kotlin.jvm.Throws;


/**
 * NickNameFragment is the fragment responsible for showing the previously contacted
 * nicknames.
 * A simple {@link Fragment} subclass.
 */
public class NickNameFragment extends Fragment
{


    /**
     * context that the nickname fragment will be using.
     */
    private Context classContext;

    /**
     * Arraylist of names.
     */
    private ArrayList<String> names = new ArrayList<>();

    private LiveData<List<Peer>> peers;

    private ArrayAdapter adapter;
    /**
     * Default constructor. Nothing will be used here.
     */
    public NickNameFragment()
    {
        //Nothing will be done here.
    }


    /**
     *
     * @param inflater inflator that came with with this method.
     * @param container a container that came with this method.
     * @param savedInstanceState just a bundle that came with this method.
     * @return view for the nickname.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState)
    {
        //Inflate a view.
        View view = inflater.inflate(R.layout.fragment_nick_name, container, false);

        //Get the context from the view.
        classContext = view.getContext();

        //Inilizes listview for nicknames here.
        ListView names = (ListView) view.findViewById(R.id.nameListView);

        //Sets the adapter for the listview.
        names.setAdapter(getNickNameAdapter());

        //Set the click listeners for each of the lists.
        names.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                PopUp(i, adapter);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /** Creates an array adapter from the database and returns it.
     * @return a newly created array adapter created from data.
     */
    private ArrayAdapter getNickNameAdapter()
    {

        //TODO get number of names from database here.
        int size = 0; //Change later.

        //TODO extract nicknames from database here.

        //Adapter that will be used.
        adapter = (new ArrayAdapter(classContext,
                android.R.layout.simple_list_item_1,
                names));

        ViewModel viewModel = getViewModel(getActivity().getApplication());

        if(peers == null) {
            peers = viewModel.getPeers();
            peers.observeForever(new Observer<List<Peer>>()
            {
                @Override
                public void onChanged(List<Peer> peers)
                {
                    if(peers != null) {
                        names.clear();
                        for(int i = 0;i<peers.size();i++) {
                            names.add(peers.get(i).macAddress + " - " + peers.get(i).nickname);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        //Returns the new arrayadapter.
        return adapter;
    }

    /**
     * Creates a pop up and sets up onclicklisteners.
     * @param index position of the name being changed.
     */
    private void PopUp(final int index, ArrayAdapter adapter) {
        NickNameModal nick = new NickNameModal(this, adapter, peers.getValue(), index);
        nick.show();
    }


    /**
     * Checks to make sure that no method has the same name.
     * @return false.
     */
    private boolean noSameName() {
        return false;
    }


    /**
     * For testing
     * @param app
     * @return
     */
    public ViewModel getViewModel(Application app) {
        return new ViewModel(app);
    }





}

