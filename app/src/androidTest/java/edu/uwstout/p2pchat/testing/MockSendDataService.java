package edu.uwstout.p2pchat.testing;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * A class which mocks the SendDataService for the sake of the
 * dependency injection design / testing pattern.
 */
public final class MockSendDataService extends IntentService
{

    public MockSendDataService()
    {
        super("MockSendDataService");
    }

    /**
     * Sends the Intent back to the instrumented test
     * for parsing and validating.
     * @param intent The intent passed that needs to be tested.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        // yeah, I don't know man.
        assert(false);
    }
}
