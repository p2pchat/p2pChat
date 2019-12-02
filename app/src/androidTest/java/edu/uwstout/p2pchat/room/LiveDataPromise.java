package edu.uwstout.p2pchat.room;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;

/**
 * Class used for unwrapping LiveData objects for testing
 * @param <T>
 */
public class LiveDataPromise<T> {
    T result;
    LiveData<T> liveData;
    CountDownLatch latch;

    /**
     * Constructor taking in LiveData object to be unwrapped
     * @param liveData
     */
    public LiveDataPromise(LiveData<T> liveData) {
        latch = new CountDownLatch(1);
        this.liveData = liveData;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                liveData.observeForever(new Observer<T>() {
                    @Override
                    public void onChanged(T t) {
                        result = t;
                        latch.countDown();
                    }
                });
            }
        });
    }

    /**
     * Blocks until the LiveData result is ready. Then returns the value.
     * @return
     */
    T await() {
        try {
            latch.await();
            return result;
        } catch(Exception e) {
            return null;
        }
    }
}
