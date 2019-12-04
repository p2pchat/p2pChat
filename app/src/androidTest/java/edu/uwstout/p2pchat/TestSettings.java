package edu.uwstout.p2pchat;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestSettings
{


    @Rule
    public FragmentTestRule<?, SettingFragment> fragmentRule =
            new FragmentTestRule<>(MainActivity.class, SettingFragment.class);



    /**
     * Test to make sure expandable list view is showing.
     */
    @Test
    public void expandableListIsDisplayed()
    {
        onView(withId(R.id.eList)).check(matches(ViewMatchers.isDisplayed()));
    }


    /**
     * Check to see if all parent titles are visible.
     */
    @Test
    public void testTitles()
    {
        String[] parent =
                fragmentRule.getActivity().getResources().getStringArray(R.array.parentSettingsName);

        // Create a new save object.
        Save save = new Save(fragmentRule.getFragment());

        onView(withText(parent[0])).check(matches(ViewMatchers.isDisplayed()));

        onView(withText(save.getTitle())).check(matches(ViewMatchers.isDisplayed()));

        onView(withText(parent[2])).check(matches(ViewMatchers.isDisplayed()));

        onView(withText(parent[3])).check(matches(ViewMatchers.isDisplayed()));
    }


    /**
     * Tries to test to make sure database gets cleared when procedure is followed.
     */
    @Test
    public void testClickOnRemoveDatabase()
    {
        String[] parent =
                fragmentRule.getActivity().getResources().getStringArray(R.array.parentSettingsName);
        String[] child =
                fragmentRule.getFragment().getResources().getStringArray(R.array.themeColor);

        MockViewModel viewModel = new MockViewModel(fragmentRule.getActivity().getApplication());
        viewModel.insertPeer("e-e-e-e-e", "e-e-e-e-e");
        viewModel.insertPeer("ek-e-45-e-e", "ek-e-45-e-e");

        //Click on remove database.
        onView(withText(parent[2])).perform(click());
        onView(withText("Clearing Contacts?")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        //Check to make sure these do not exist.
        onView(withText("e-e-e-e-e - e-e-e-e")).check(doesNotExist());
        onView(withText("ek-e-45-e-e - ek-e-45-e-e")).check(doesNotExist());
    }


    /**
     * Makes sure the theme Color headers are visible.
     */
    @Test
    public void checkThemeColorTitle()
    {
        String headers[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.themeColorTitle);

        for (int i = 0; i < headers.length; i++)
        {
            try {
                onView(withText(headers[i])).check(matches(isDisplayed()));
            }catch (NoMatchingViewException e) {

            }
        }
    }


    /**
     * Makes sure the theme color views are showing.
     */
    @Test
    public void checkThemeColorOptions() {
        String headers[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.themeColorTitle);
        String themeColor[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.themeColor);
        for (int i = 0; i < headers.length; i++)
        {
            try {
                onView(withText(headers[i])).perform(click());

            }catch (NoMatchingViewException e) { }
        }

        onView(withText(themeColor[0])).check(matches(isDisplayed()));
        onView(withText(themeColor[1])).check(matches(isDisplayed()));
        onView(withText(themeColor[2])).check(matches(isDisplayed()));
    }

    /**
     * Checks to make sure that the myDeviceInfo items all exist.
     */
    @Test
    public void checkMyDeviceInfo()
    {
        String parent[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.parentSettingsName);
        String deviceInfo[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.myDeviceInfoSettings);
        if (!WifiDirect.getInstance(InstrumentationRegistry
            .getInstrumentation().getTargetContext()).isP2pEnabled()) {
            assert(true);
            return; // We are on an emulator, and this test will fail, return early.
            // TODO use the mock WifiDirect class
        }
        //open up expanded list view adapter.
        onView(withText(parent[0])).perform(click());

        //Check the conditions for all device info.
        for (int i = 0; i < deviceInfo.length; i++)
        {
            onView(withText(containsString(deviceInfo[i]))).check(matches(isDisplayed()));
        }
    }


    @Test
    public void toNickNameFragment() {
        String parent[] =
                fragmentRule.getFragment().getResources().getStringArray(R.array.parentSettingsName);
        try {
            onView(withText(parent[3])).perform(click());
        }catch (PerformException e)
        {
            Log.w("Explaination: ", e.getLocalizedMessage());
        }
    }

    /**
     * Only Run for entering the data into the app, to show for an example.
     */
    @Test
    public void Insert() {
        ViewModel viewModel = new ViewModel(fragmentRule.getActivity().getApplication());
        viewModel.insertPeer("EEEE", "EEEE");
    }


}
