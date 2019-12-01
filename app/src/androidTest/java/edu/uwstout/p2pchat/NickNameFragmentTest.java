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
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;


import android.app.Application;
import android.content.Context;

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
    public void mockDataEnter()
    {
        ViewModel viewModel = new ViewModel(fragmentRule.getActivity().getApplication());
        onView(withId(R.id.nameListView)).check(matches(isDisplayed()));
        onData(withText(MockPeers.nick.macAddress + " - " + MockPeers.nick.nickname)).check(matches(isDisplayed()));
    }


    public NickNameModal getNickNameModal(Context context, String macaddress)
    {
        return new NickNameModalTest(context, macaddress);
    }


    public ViewModel getViewModel(Application app)
    {
        return new MockViewModel(app);
    }

}

