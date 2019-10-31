package edu.uwstout.p2pchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Sets up and shows the NickName Modal.
 * @author Nick Zolondek
 */
public class NickNameModal
{
    //Activity being used.
    private Activity mActivity;

    //Builder of AlertDialogs.
    private AlertDialog.Builder dialogBuilder;

    // Alert dialog.
    private AlertDialog dialog;


    /**
     * Requires 4 parameters. Will set up dialog here and
     * action listeners.
     * @param fragment fragment of the screen being displayed.
     * @param adapter listview adapter that is being presently used.
     * @param names names of the database.
     * @param index index being updated.(Where the listview has last been touched).
     */
    public NickNameModal(final Fragment fragment, final ArrayAdapter adapter,
            ArrayList<String> names, final int index) {
        //Note: Everything in here is constant and shall not change depending on where it is called.


        // Extract activity from fragment.
        Context context = fragment.getContext();
        mActivity = fragment.getActivity();

        // Creates a builder that will build alert dialogs.
        dialogBuilder = new AlertDialog.Builder(context);

        //Gets the view from the xml file.
        final View view1 = fragment.getLayoutInflater().inflate(R.layout.rename_dialog, null);

        //Sets the view1 onto the dialog.
        dialogBuilder.setView(view1);

        //Sets the nickname.
        dialogBuilder.setTitle("Set NickName");

        //Set button listener.
        SetButtonListeners(view1, adapter, names, index);

        //sets the dialog to this.
        dialog = dialogBuilder.create();

    }


    /**
     * Sets the button listeners.
     */
    private void SetButtonListeners(final View view, final ArrayAdapter adapter,
            final ArrayList<String> names, final int index) {

        // Button: Rename
        dialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                final String NICKNAME =
                        ((EditText)view.findViewById(R.id.newNickName)).getText().toString();

                //TODO update the database here with NICKNAME.

                //updates the names from the listview.
                names.set(index, NICKNAME);

                //Notifies adapter that the data changed, so it is solely
                //responsible for updating listview.
                adapter.notifyDataSetChanged();
            }
        });

        //allow canceling without any special code.
        dialogBuilder.setNegativeButton("Cancel", null);
    }


    /**
     *
     * Shows the context
     */
    public void show() {
        //Shows the dialog.
        dialog.show();

        //Required classes to be able to retrieve the screen dimensions.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //Get screen width - 10;
        final int WIDTH = displayMetrics.widthPixels - 10;

        //Get screen height.
        final int HEIGHT = displayMetrics.heightPixels / 3;

        //Sets the width and height.
        dialog.getWindow().setLayout(WIDTH, HEIGHT);
    }






}
