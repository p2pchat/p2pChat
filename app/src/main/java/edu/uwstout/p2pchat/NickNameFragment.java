package edu.uwstout.p2pchat;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Annotations.DatabaseRequired;
import Annotations.Fix;
import Annotations.NotDone;
import Annotations.Priority;


/**
 * NickNameFragment is the fragment responsible for showing the previously contacted
 * nicknames.
 * A simple {@link Fragment} subclass.
 */
@NotDone
public class NickNameFragment extends Fragment
{
    /**
     * The listview that had been initilized in the xml file.
     */
    private ListView view;

    /**
     * context that the nickname fragment will be using.
     */
    private Context classContext;


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

        //Put out on the screen that this is lauching.
        Log.i("Launching", "Nickname view had launched");

        //Inilizes listview for nicknames here.
        ListView names = (ListView) view.findViewById(R.id.nameListView);

        //Sets the adapter for the listview.
        names.setAdapter(getNickNameAdapter());

        setClickListener(names);

        // Inflate the layout for this fragment
        return view;
    }

    /** Creates an array adapter from the database and returns it.
     * @return a newly created array adapter created from data.
     */
    @DatabaseRequired(why = "Need to extract information from the database" +
            " and put it names.")
    private ArrayAdapter getNickNameAdapter()
    {
        ArrayList<String> names = new ArrayList<>();

        //TODO remove later
        names.add("Nick");
        names.add("Dr. D");
        names.add("Dr. Mason");
        names.add("Austin");
        names.add("Evan");


        //TODO get number of names from database here.
        int size = 0; //Change later.

        //TODO extract nicknames from database here.




        //Returns the new arrayadapter.
        return (new ArrayAdapter(classContext,
                android.R.layout.simple_list_item_1,
                names));
    }


    /**
     * Seperate method to set the click listener for the.
     */
    private void setClickListener(ListView view)
    {
        final Fragment FRAGMENT = this;
        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i,
                    final long l)
            {
                //Creates a rename dialog from the class.
                RenameDialog dialog = new RenameDialog(classContext, FRAGMENT);

                //Shows the popup.
                dialog.show();
            }
        });
    }


}
