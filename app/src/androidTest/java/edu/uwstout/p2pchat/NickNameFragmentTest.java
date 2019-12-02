package edu.uwstout.p2pchat;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NickNameFragmentTest
{

    @Rule
    public FragmentTestRule<MainActivity, TestNickNameFragment> fragmentRule =
            new FragmentTestRule<>(MainActivity.class, TestNickNameFragment.class);
    String testNickName = "The Nick";

    /**
     * Resets mock data in the MockViewModel after a test
     */
    @After
    public void after()
    {
        MockViewModel.resetModel();
    }

    /**
     * Tests that changes a nickname works
     */
    @Test
    public void changeNickname()
    {
        onView(withText(MockPeers.nick.macAddress + " - " + MockPeers.nick.nickname)).perform(
                click());
        onView(withId(R.id.newNickName)).perform(typeText(testNickName));
        onView(withText(R.string.yes)).perform(click());
        onView(withText(MockPeers.nick.macAddress + " - " + testNickName)).check(
                matches(isDisplayed()));
    }
}

