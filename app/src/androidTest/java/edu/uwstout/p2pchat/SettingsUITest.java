package edu.uwstout.p2pchat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingsUITest
{

    @Rule
    public ActivityTestRule<MainActivity> mSettingFragmentActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void settingsDisplayed()
    {

        onView(withId(R.id.eList)).check(matches(isDisplayed()));

    }

    @Test
    public void settingsIsTouchable()
    {


    }


}
