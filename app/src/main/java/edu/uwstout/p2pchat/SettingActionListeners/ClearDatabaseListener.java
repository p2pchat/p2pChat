package edu.uwstout.p2pchat.SettingActionListeners;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.uwstout.p2pchat.ViewModel;

public class ClearDatabaseListener implements DialogInterface.OnClickListener
{
    /**
     * Current fragment.
     */
    private Fragment mFragment;

    /**
     * Assign fragment to global fragment, so can be accessed for getting application.
     *
     * @param fragment
     *         fragment being used.
     */
    public ClearDatabaseListener(Fragment fragment)
    {
        mFragment = fragment;
    }

    /**
     * Responsible for clearing database.
     *
     * @param dialogInterface
     *         the dialogInterface which will not be in use.
     * @param i
     *         this will not be used either, but is required.
     */
    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {
        // View model to access the database.
        ViewModel mViewModel = new ViewModel(mFragment.getActivity().getApplication());

        //Delete Everything.
        mViewModel.deleteEverything();

        // Create a toast.
        Toast toast = Toast.makeText(mFragment.getContext(), "Cleared All Contacts!",
                Toast.LENGTH_SHORT);

        //Show Toast.
        toast.show();
    }
}
