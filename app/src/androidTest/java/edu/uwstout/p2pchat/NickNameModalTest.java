package edu.uwstout.p2pchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class NickNameModalTest extends NickNameModal
{


    /**
     * Test Nickname modal.
     * @param context context to show dialog.
     * @param macAddress what needs to be changed.
     */
    public NickNameModalTest(Context context, String macAddress)
    {
        super(context, macAddress);
    }

    /**
     * Get the view model.
     * @param app application.
     * @return new MockViewModel.
     */
    @Override
    public ViewModel getViewModel(Application app)
    {
        return new MockViewModel(app);
    }
}
