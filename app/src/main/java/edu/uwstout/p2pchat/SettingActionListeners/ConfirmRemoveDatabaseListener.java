package edu.uwstout.p2pchat.SettingActionListeners;

import android.app.AlertDialog;
import android.view.View;

import androidx.fragment.app.Fragment;


/**
 * A On Clicklistener class mean't for clearing the database.
 */
public class ConfirmRemoveDatabaseListener implements View.OnClickListener
{


    /**
     * The frament being used.
     */
    Fragment mFragment;

    /**
     * Clear All database.
     */
    public ConfirmRemoveDatabaseListener(Fragment fragment)
    {
        //Fragment being used.
        mFragment = fragment;
    }

    /**
     * What happens when view is click happens here.
     *
     * @param view
     *         view being accessed.
     */
    @Override
    public void onClick(View view)
    {
        //Show alert when pressed
        getAlert().show();
    }


    /**
     * Get alert dialog.
     *
     * @return the view.
     */
    public AlertDialog getAlert()
    {
        //Pop up for if the user really wants to delete the database.
        AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getContext());

        //Set the title.
        builder.setTitle("Clearing Contacts?");

        //Set the message.
        builder.setMessage("Are you sure you want to clear all your contacts?");

        //Set the cancel button.
        builder.setNegativeButton("No", null);

        //Set the positive button.
        builder.setPositiveButton("Yes",
                new ClearDatabaseListener(mFragment));

        //Create an alert dialog.
        return builder.create();
    }

}
