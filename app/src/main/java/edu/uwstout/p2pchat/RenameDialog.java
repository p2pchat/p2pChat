package edu.uwstout.p2pchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import Annotations.Blocked;
import Annotations.DatabaseRequired;
import Annotations.Todo;

/**
 * A simple class that just gives you the ease of letting the popover do its
 * thing.
 */
@Todo(item1 = "Must come back and add it with database features.")
class RenameDialog
{
    /**
     * Context needed to display the screen on.
     */
    private Context context;

    /**
     * Alert dialog that will be
     */
    private AlertDialog mDialog;

    /**
     * Adds the name to the listView.
     */
    private String addedName;

    /**
     * Constructor that requires all that it needs.
     *
     * @param context
     *         context that the alert will popover.
     * @param fragment
     *         the fragment that is currently in use.
     */
    @SuppressWarnings("checkstyle:LineLength")
    public RenameDialog(final Context context, final Fragment fragment)
    {
        //Note: Everything in here is constant and shall not change depending on where it is called.
        // Creates a builder that will build alert dialogs.
        final AlertDialog.Builder BUILDER = new AlertDialog.Builder(context);

        //Sets the nickname.
        BUILDER.setTitle("Set NickName");

        // Gets the inflator.
        View view1 = fragment.getLayoutInflater().inflate(R.layout.rename_dialog, null);
        final EditText NICKNAME = (EditText) view1.findViewById(R.id.newNickName);

        //Sets the view.
        BUILDER.setView(view1);

        //Sets the button for dialog.
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
                    dialogInterface.dismiss();
                }
                else
                {
                    //Add the name to the list.
                    addedName = NICKNAME.getText().toString();
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
        mDialog = BUILDER.create();
    }


    /**
     * Shows the popover to the screen and resizes it to fit well.
     */
    @SuppressWarnings({"checkstyle:OperatorWrap", "checkstyle:LineLength"})
    public void show()
    {
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
            Toast.makeText(context, "Error resizing screen.", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Check to see if there is no same name previously
     * listed in the database.
     */
    @Blocked
    @DatabaseRequired
    private boolean noSameName()
    {
        return false;
    }


    /**
     * Gets the name.
     * @return the newly added name.
     */
    public String getName()
    {
        return addedName;
    }
}
