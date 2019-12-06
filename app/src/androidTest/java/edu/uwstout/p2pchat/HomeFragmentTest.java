package edu.uwstout.p2pchat;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.View;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;
import edu.uwstout.p2pchat.testing.MockWifiDirect;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest
{

    /**
     * From: https://stackoverflow.com/a/39756832
     * @param matcher
     * @param index
     * @return
     */
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @Rule
    public FragmentTestRule<MainActivity, TestHomeFragment> fragmentRule = new FragmentTestRule<>(MainActivity.class, TestHomeFragment.class);

    @Before
    public void setup() {

        WifiP2pDevice device = new WifiP2pDevice();
        device.deviceAddress = MockPeers.austin.macAddress;
        device.deviceName = "Mock peer";
        device.primaryDeviceType = "Mock device";
        List<WifiP2pDevice> peers = new ArrayList<>();
        peers.add(device);
        MockWifiDirect.setPeerDevices(peers);
    }

    @After
    public void after()
    {
        MockViewModel.resetModel();
        MockWifiDirect.reset();
        TestHomeFragment.reset();
    }

    @Test
    public void testDiscoveryList()
    {
        assert(!TestHomeFragment.getNavigated());
        TestHomeFragment.tapGetPeers();
        onView(withIndex(withId(R.id.unrecognized_list), 0)).perform(click());
        assert(TestHomeFragment.getNavigated());
        assert(TestHomeFragment.getNavigationDevice().deviceAddress.equals(MockPeers.austin.macAddress));
    }

}

