package edu.uwstout.p2pchat;


import android.app.AlertDialog;
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

import java.util.ArrayList;



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
        //Creates the mock data.
        setNames();

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


        //Returns the new arrayadapter.
        return adapter;
    }



    /**
     * Assigns all names to the arraylist.
     */
    private void setNames() {
        //Set names here.
        //TODO read from database here.


        //TODO remove everything from the list.
        names.add("Nick");
        names.add("Dr. D");
        names.add("Dr. Mason");
        names.add("Austin");
        names.add("Evan");

    }




    /**
     * Creates a pop up and sets up onclicklisteners.
     * @param index position of the name being changed.
     */
    private void PopUp(final int index, ArrayAdapter adapter) {
        NickNameModal nick = new NickNameModal(this, adapter, names, index);
        nick.show();
    }


    /**
     * Checks to make sure that no method has the same name.
     * @return false.
     */
    private boolean noSameName() {
        return false;
    }








}

