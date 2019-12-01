package edu.uwstout.p2pchat.SettingActionListeners;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.uwstout.p2pchat.R;

/**
 * Renaming nick name listview item goes here.
 */
public class NickNameListener implements View.OnClickListener
{
    /**
     * Fragment that is used for transitioning to different fragments.
     */
    private Fragment mFragment;

    /**
     * Constructor that requires a fragment to find navigation controller.
     */
    public NickNameListener(Fragment fragment)
    {
        mFragment = fragment;
    }


    /**
     * We want to change views when rename option of settings is clicked.
     *
     * @param view
     *         view that is clicked.
     */
    @Override
    public void onClick(View view)
    {
        // Find the nav controller.
        NavController nav = Navigation.findNavController(mFragment.getActivity(),
                R.id.mainNavHostFragment);

        // Navigate to the nick name fragment/page.
        nav.navigate(R.id.toNickNameFragment);
    }
}
