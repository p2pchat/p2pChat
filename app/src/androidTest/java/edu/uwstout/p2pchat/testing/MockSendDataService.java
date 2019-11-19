package edu.uwstout.p2pchat.testing;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.util.concurrent.CountDownLatch;

/**
 * A class which mocks the SendDataService for the sake of the
 * dependency injection design / testing pattern.
 */
public final class MockSendDataService extends IntentService
{

    public MockSendDataService()
    {
        super("MockSendDataService");
        latch = new CountDownLatch(1);
    }

    /**
     * Sends the Intent back to the instrumented test
     * for parsing and validating.
     *
     * @param intent
     *         The intent passed that needs to be tested.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        updateStaticIntent(intent);
    }

    /*
        So, in order to test the Intent passed to the
        MockSendDataService, we need some means of
        passing data to the testing thread, because
        this is a multithreaded environment that we
        are testing. Therefore, all this static
        functionality is an attempt to pass data out
        of thread.
     */
    private static Intent intent;
    private static CountDownLatch latch;

    synchronized private static void updateStaticIntent(Intent i)
    {
        intent = i;
        latch.countDown();
    }

    public static Intent await()
    {
        try
        {
            // wait for the intent to be updated.
            latch.await();
            // Make a copy of the intent.
            Intent intentCopy = intent;
            // reset the original for future use if needed.
            updateStaticIntent(null);
            latch = new CountDownLatch(1);
            // return the copy.
            return intentCopy;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
