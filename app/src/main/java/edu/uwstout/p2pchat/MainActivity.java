package edu.uwstout.p2pchat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import Annotations.BugAlert;
import edu.uwstout.p2pchat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    // Necessary for consistency between methods
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private static final String LOG_TAG = "MainActivity";

    // private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;

    /**
     * A lifecycle function which creates the view
     *
     * @param savedInstanceState
     *         a Bundle which contains information saved by the OS
     */
    @BugAlert
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.drawerLayout = binding.drawerLayout;
        final NavController navController =
                Navigation.findNavController(this, R.id.mainNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, this.drawerLayout);

        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener()
                {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller,
                            @NonNull NavDestination destination, @Nullable Bundle arguments)
                    {
                        if (destination.getId() == navController.getGraph().getStartDestination()) {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        } else {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        }
                    }
                });
        NavigationUI.setupWithNavController(binding.mainNavView, navController);
        // TODO this is causing an app crash, revisit this in a future sprint since this wasn't
        //  an explicit goal of this sprint
        // Check if the app has permissions to use location data, and ask for it if we don't.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MainActivity.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            // We don't handle the response here, we receive it in onRequestPermissionsResult
        }
    }

    /**
     * Handles responses when we ask for permissions from the user to use functionality
     * of the OS such as location data.
     *
     * @param requestCode
     *         an integer representing what request is being responded to.
     * @param permissions
     *         a string array of permissions asked for in that request.
     * @param grantResults
     *         an integer array, either PackageManager.PERMISSION_GRANTED or
     *         PackageManager.PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        // Use of switch case to allow us to expand this method later if needed.
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(LOG_TAG,
                            "Coarse Location permission not granted. Unable to use WiFi Direct " +
                                    "features");
                    finish(); // closes the activity
                }
                break;
            default:
                Log.e(LOG_TAG, "Unhandled permissions result: " + requestCode);
        }
    }

    /**
     * Makes it possible to use the up button in the top left corner of the screen
     * @return a boolean indicating if the fragment can move up on the back stack.
     */
    @Override
    public boolean onSupportNavigateUp()
    {
        final NavController navController = Navigation.findNavController(this, R.id.mainNavHostFragment);
        return NavigationUI.navigateUp(navController, drawerLayout);
    }


}
