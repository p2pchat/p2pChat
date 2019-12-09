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

    public LiveDataPromise(LiveData<T> liveData) {
        this(liveData, 1);
    }
    /**
     * Constructor taking in LiveData object to be unwrapped
     * @param liveData
     */
    public LiveDataPromise(LiveData<T> liveData, int changesExpected) {
        latch = new CountDownLatch(changesExpected);
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
    public T await() {
        try {
            latch.await();
            return result;
        } catch(Exception e) {
            return null;
        }
    }
}
