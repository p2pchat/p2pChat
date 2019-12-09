package edu.uwstout.p2pchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.Looper;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.test.platform.app.InstrumentationRegistry;

import edu.uwstout.p2pchat.testing.MockViewModel;
import edu.uwstout.p2pchat.testing.MockWifiDirect;

public class TestHomeFragment extends HomeFragment {

    static TestHomeFragment instance = null;
    static void tapGetPeers() {
        if(instance == null) return;
        MenuItem menuItem = new MenuItem()
        {
            @Override
            public int getItemId()
            {
                return R.id.refresh_peers;
            }

            @Override
            public int getGroupId()
            {
                return 0;
            }

            @Override
            public int getOrder()
            {
                return 0;
            }

            @Override
            public MenuItem setTitle(CharSequence title)
            {
                return null;
            }

            @Override
            public MenuItem setTitle(int title)
            {
                return null;
            }

            @Override
            public CharSequence getTitle()
            {
                return null;
            }

            @Override
            public MenuItem setTitleCondensed(CharSequence title)
            {
                return null;
            }

            @Override
            public CharSequence getTitleCondensed()
            {
                return null;
            }

            @Override
            public MenuItem setIcon(Drawable icon)
            {
                return null;
            }

            @Override
            public MenuItem setIcon(int iconRes)
            {
                return null;
            }

            @Override
            public Drawable getIcon()
            {
                return null;
            }

            @Override
            public MenuItem setIntent(Intent intent)
            {
                return null;
            }

            @Override
            public Intent getIntent()
            {
                return null;
            }

            @Override
            public MenuItem setShortcut(char numericChar, char alphaChar)
            {
                return null;
            }

            @Override
            public MenuItem setNumericShortcut(char numericChar)
            {
                return null;
            }

            @Override
            public char getNumericShortcut()
            {
                return 0;
            }

            @Override
            public MenuItem setAlphabeticShortcut(char alphaChar)
            {
                return null;
            }

            @Override
            public char getAlphabeticShortcut()
            {
                return 0;
            }

            @Override
            public MenuItem setCheckable(boolean checkable)
            {
                return null;
            }

            @Override
            public boolean isCheckable()
            {
                return false;
            }

            @Override
            public MenuItem setChecked(boolean checked)
            {
                return null;
            }

            @Override
            public boolean isChecked()
            {
                return false;
            }

            @Override
            public MenuItem setVisible(boolean visible)
            {
                return null;
            }

            @Override
            public boolean isVisible()
            {
                return false;
            }

            @Override
            public MenuItem setEnabled(boolean enabled)
            {
                return null;
            }

            @Override
            public boolean isEnabled()
            {
                return false;
            }

            @Override
            public boolean hasSubMenu()
            {
                return false;
            }

            @Override
            public SubMenu getSubMenu()
            {
                return null;
            }

            @Override
            public MenuItem setOnMenuItemClickListener(
                    OnMenuItemClickListener menuItemClickListener)
            {
                return null;
            }

            @Override
            public ContextMenu.ContextMenuInfo getMenuInfo()
            {
                return null;
            }

            @Override
            public void setShowAsAction(int actionEnum)
            {

            }

            @Override
            public MenuItem setShowAsActionFlags(int actionEnum)
            {
                return null;
            }

            @Override
            public MenuItem setActionView(View view)
            {
                return null;
            }

            @Override
            public MenuItem setActionView(int resId)
            {
                return null;
            }

            @Override
            public View getActionView()
            {
                return null;
            }

            @Override
            public MenuItem setActionProvider(ActionProvider actionProvider)
            {
                return null;
            }

            @Override
            public ActionProvider getActionProvider()
            {
                return null;
            }

            @Override
            public boolean expandActionView()
            {
                return false;
            }

            @Override
            public boolean collapseActionView()
            {
                return false;
            }

            @Override
            public boolean isActionViewExpanded()
            {
                return false;
            }

            @Override
            public MenuItem setOnActionExpandListener(OnActionExpandListener listener)
            {
                return null;
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                instance.onOptionsItemSelected(menuItem);
            }
        });
    }

    static boolean navigated = false;
    static WifiP2pDevice navigationDevice = null;
    public static void reset() {
        navigated = false;
        navigationDevice = null;
    }
    public static boolean getNavigated() {
        return navigated;
    }
    public static WifiP2pDevice getNavigationDevice() {
        return navigationDevice;
    }

    public TestHomeFragment()
    {
        super();
        instance = this;
    }

    /**
     * Get instance of WifiDirect. Overridable for testing.
     *
     * @param context
     * @return
     */
    @Override
    WifiDirect getWifiDirect(@NonNull Context context) {
        return new MockWifiDirect(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }


    ViewModel getViewModel() {
        return new MockViewModel(null);
    }

    @Override
    public void addMenuItem(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Do nothing
    }

    /**
     * Overridable for testing
     *
     * @param device
     */
    @Override
    public void navigateToChatFragment(WifiP2pDevice device) {
        navigated = true;
        navigationDevice = device;
    }
}
