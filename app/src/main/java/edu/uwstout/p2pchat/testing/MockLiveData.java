package edu.uwstout.p2pchat.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.function.Function;

public class MockLiveData<T> extends LiveData<T> {
    private MockDataUpdater<T> updater;

    public MockLiveData(MockDataUpdater<T> updater) {
        super();
        this.updater = updater;
        super.setValue(updater.update());
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        super.observe(owner, observer);
        update();
    }

    @Override
    public void observeForever(@NonNull Observer observer) {
        super.observeForever(observer);
        update();
    }

    public void update() {
        super.postValue(updater.update());
    }
}
