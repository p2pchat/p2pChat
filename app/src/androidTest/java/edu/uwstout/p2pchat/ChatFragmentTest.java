package edu.uwstout.p2pchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest
{

    private String testingString;
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initTextandFragment()
    {
        testingString = "Hello";
        FragmentTransaction transaction =
                activityRule.getActivity().getSupportFragmentManager().beginTransaction();
        ChatFragment chatFragment = new ChatFragment();
        transaction.add(chatFragment,"chatFrag");
        transaction.commit();
    }

    @Test
    public void validateInputText()
    {
        onView(withId(R.id.textInput)).perform(typeText(testingString)).check(
                matches(withText(testingString)));
    }

    @Test
    public void validateSendButtonCreatesMessage()
    {
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.messagesRecyclerView)).check(matches(hasChildCount(1)));

    }
    @Test
    public void validateEditTextClearsOnButtonPress(){
        onView(withId(R.id.textInput)).perform(typeText(testingString));
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.textInput)).check(matches(withText("")));
    }
    @Test
    public void validateImageButtonClicks(){
        onView(withId(R.id.attachmentButton)).perform(click()).check(matches(isDisplayed()));
    }


}

