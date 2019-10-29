package edu.uwstout.p2pchat;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
     * The listview that had been initilized in the xml file.
     */
    private ListView view;

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
        setNames();

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
     * Seperate method to set the click listener for the.
     */
    private void setClickListener(ListView view)
    {
        //Onclick listener for all values of the list.
        view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i,
                    final long l)
            {
                Log.i("LOGGG", String.valueOf(i));
                //Dialog.
                PopUp(i);
            }
        });
    }

    /**
     * Assigns all names to the arraylist.
     */
    private void setNames() {
        //Set names here.
        //TODO remove later
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
    private void PopUp(final int index) {

        //Note: Everything in here is constant and shall not change depending on where it is called.
        // Creates a builder that will build alert dialogs.
        final AlertDialog.Builder BUILDER = new AlertDialog.Builder(this.classContext);

        //Sets the nickname.
        BUILDER.setTitle("Set NickName");

        // Gets the inflator.
        View view1 = this.getLayoutInflater().inflate(R.layout.rename_dialog, null);
        final EditText NICKNAME = (EditText) view1.findViewById(R.id.newNickName);

        //Sets the view.
        BUILDER.setView(view1);


        //Sets the button listener for dialog.
        BUILDER.setPositiveButton("Change", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i)
            {
                //TODO later
                Log.w("Running", NICKNAME.getText().toString());

                //Check if the text is not empty.
                if (NICKNAME.getText().toString().isEmpty() && noSameName())
                {
                    //Cancel.
                    dialogInterface.dismiss();
                }
                else
                {
                    //Add the name to the list.
                    String nName = NICKNAME.getText().toString();
                    update(nName, index);


                }
            }
        });

        //Sets the button click listener for canceling.
        BUILDER.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i)
            {
                //Logs the cancel onto the screen.
                Log.i("Cancel", "Cancelling");
                dialogInterface.cancel();
            }
        });


        //sets the dialog to this.
        Dialog mDialog = BUILDER.create();


        //Width and height for screen size.
        final int WIDTH = 1000;
        final int HEIGHT = 600;

        //Shows the dialog.
        mDialog.show();

        //Attempt to catch a Null pointer exception.
        try
        {
            mDialog.getWindow().setLayout(WIDTH, HEIGHT);
        }
        catch (NullPointerException e)
        {
            //Output in console of the null pointer exception.
            Log.w("Null pointer Exception", "Location: Setting layout size for RenameDialog.java."
                    +
                    " More Info: " + e.getMessage());
            //Let user know.
            Toast.makeText(classContext, "Error resizing screen.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Checks to make sure that no method has the same name.
     * @return
     */
    private boolean noSameName() {
        return false;
    }


    /**
     * Updates the names when it is changed.
     * @param name new name.
     * @param index index of the new name.
     */
    private void update(String name, int index) {
        names.set(index, name);
        adapter.notifyDataSetChanged();
    }





}

