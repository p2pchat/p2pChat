package edu.uwstout.p2pchat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    // Necessary for consistency between methods
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;

    /**
     * A lifecycle function which creates the view
     *
     * @param savedInstanceState
     *         a Bundle which contains information saved by the OS
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Check if the app has permissions to use location data, and ask for it if we don't.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MainActivity.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
//            // We don't handle the response here, we receive it in onRequestPermissionsResult
//        }
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
                    Log.e("WiFiDirectActivity",
                            "Coarse Location permission not granted. Unable to use WiFi Direct " +
                                    "features");
                    finish(); // closes the activity
                }
                break;
            default:
                Log.e("WiFiDirectActivity", "Unhandled permissions result: " + requestCode);
        }
    }

}
