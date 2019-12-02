package edu.uwstout.p2pchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
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


/**
 * Sets up and shows the NickName Modal.
 * @author Nick Zolondek
 */
public class NickNameModal
{
    /**
     * A global AlertDialog.
     */
    AlertDialog dialog;

    /**
     * Builds a nickname modal.
     * @param context context being used.
     * @param macAddress mac address that needs to be changed.
     */
    public NickNameModal(Context context, String macAddress)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.setNickName);

        // Assign the inflated view to a view.
        View view = View.inflate(context, R.layout.rename_dialog, null);

        //Add the text field to the dialog.
        builder.setView(view);

        //Add an action to YES button.
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int p)
            {
                //Incase if the application fails to load.
                try
                {
                    //Convert applicationContext to application.
                    Application app = (Application) context.getApplicationContext();

                    //View model that will be used.
                    ViewModel model = getViewModel(app);

                    //Retrieve text from the dialog.
                    EditText text = view.findViewById(R.id.newNickName);

                    //Update the peer nickname.
                    model.insertPeer(macAddress, text.getText().toString());

                } catch (Exception e)
                {
                    // Give out a warning of where it is and why it is crashing.
                    Log.w("NicknameModal ", e.getLocalizedMessage());
                }
            }
        });

        //Set no button.
        builder.setNegativeButton(R.string.cancel, null);
        this.dialog =  builder.create();
    }

    /**
     * Show the dialog.
     */
    public void show() {
        dialog.show();
    }

    /**
     * Get the view model.
     * @param app application.
     * @return new Viewmodel.
     */
    public ViewModel getViewModel(Application app) {
        return new ViewModel(app);
    }
}
