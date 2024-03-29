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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest
{

    String testMessage1 = "This is madness!";
    String testMessage2 = "Madness? No, this is Sparta!";

    @Rule
    public FragmentTestRule<MainActivity, TestChatFragment> fragmentRule = new FragmentTestRule<>(MainActivity.class, TestChatFragment.class);

    /**
     * Resets the mock data in the MockViewModel after a test
     */
    @After
    public void after()
    {
       MockViewModel.resetModel();
    }

    /**
     * Checks that sending and receiving messages work correctly
     */
    @Test
    public void validateSendAndReceiveMessages()
    {

        onView(withId(R.id.textInput)).perform(typeText(testMessage1));
        onView(withId(R.id.sendButton)).perform(click());
        onView(withText(testMessage1)).check(matches(isDisplayed()));
        onView(withId(R.id.textInput)).check(matches(withText("")));

        // Simulate received message by adding new message to MockViewModel
        (new MockViewModel(null)).insertTextMessage(MockPeers.austin.macAddress, new Date(), false, testMessage2);

        onView(withText(testMessage2)).check(matches(isDisplayed()));

    }

    /**
     * Checks that the add image button is tappable
     */
    @Test
    public void validateImageButtonClicks()
    {
        onView(withId(R.id.attachmentButton)).perform(click()).check(matches(isDisplayed()));
    }


}

