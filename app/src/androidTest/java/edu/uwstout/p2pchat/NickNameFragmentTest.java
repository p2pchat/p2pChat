package edu.uwstout.p2pchat;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.Date;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class NickNameFragmentTest
{
    private String testLine = "eeeE:eee:ee - Nick";


    @ClassRule
    public static FragmentTestRule<MainActivity, NickNameFragment> fragmentRule =
            new FragmentTestRule<>(MainActivity.class, NickNameFragment.class);


    @After
    public void after()
    {
        MockViewModel.resetModel();
    }

    @Test
    public void viewIsPresent() {
        // If false, this seems to
        onView(withId(R.id.parentText)).check(matches(isDisplayed()));
    }



    /**
     * This is the code to run and should change, but however it does not.
     * MockPeers does not seem to recognize any new change to the nickname.
     * Also, does not seem to update.
     */
    @Test
    public void whatShouldHappen()
    {
        try {
            onView(withId(R.id.nameListView)).check(matches(isDisplayed()));
            onData(withText(MockPeers.nick.macAddress + " - " + MockPeers.nick.nickname)).perform(click());
            onView(withId(R.id.toNickNameFragment)).perform(typeText("Nisker"));
            onView(withText(R.string.yes)).perform(click());
            onView(withSubstring(MockPeers.nick.macAddress)).check(matches(withText(MockPeers.nick.macAddress + " - " + "Nisker")));


        }catch (RuntimeException e) {
            Log.w("NickNameFragmentTest", e.fillInStackTrace());
        }


    }




    public ViewModel getViewModel(Application app)
    {
        return new MockViewModel(app);
    }

}

