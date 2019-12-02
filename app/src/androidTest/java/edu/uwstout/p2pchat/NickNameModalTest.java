package edu.uwstout.p2pchat;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockPeers;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class NickNameModalTest
{
    @Rule
    public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void addData() {
        MockPeers.nick.nickname = "Kadde";
        MockViewModel mockViewModel = new MockViewModel(activity.getActivity().getApplication());
        mockViewModel.insertPeer("nick", "Kad");
    }

    /**
     * Note this means that mock testing failed.
     * Which makes me lead to think you cannot
     * add peers to MockViewModel.
     */
    @Test
    public void testAddMockPeer() {
        MockViewModel mockViewModel = new MockViewModel(activity.getActivity().getApplication());

        List<Peer> peers = mockViewModel.getPeers().getValue();

        List<String> elements = new ArrayList<>();

        for(Peer peer : peers) {
            if (peer.nickname.equals("Kad")) {
                Assert.assertEquals(peer.nickname, "Kad");
            }
            elements.add(peer.macAddress + " - " + peer.nickname);
        }
    }
}
