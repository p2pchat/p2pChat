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

    private static Intent intent;
    private static CountDownLatch latch;

    /**
     * Default constructor
     */
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

    /**
     * Updates the static intent in a thread safe manner.
     * @param i the intent to update with.
     */
    synchronized private static void updateStaticIntent(Intent i)
    {
        intent = i;
        latch.countDown();
    }

    /**
     * Gets the most recent intent passed to the mock service.
     * If an intent has not been set yet, this function will
     * block until the MockSendDataService has been given an intent.
     * @return an Intent, or null of an Interrupted Exception occurred.
     */
    public static Intent awaitIntent()
    {
        try
        {
            // wait for the intent to be updated.
            latch.await();
            return intent;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
