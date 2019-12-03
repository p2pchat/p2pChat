package edu.uwstout.p2pchat;

import android.os.BaseBundle;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestBundle extends BaseBundle
{
    Map<String, Serializable> data = new HashMap<>();
    @Override
    public void putSerializable(@Nullable String key, @Nullable Serializable value)
    {
        data.put(key, value);
    }

    public TestBundle()
    {

    }
}
