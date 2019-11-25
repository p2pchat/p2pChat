package edu.uwstout.p2pchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class TestIntent extends Intent
{
    String action;
    Bundle extras;
    public TestIntent(String action, Bundle extras) {
        this.action = action;
        this.extras = extras;
    }
    @Nullable
    @Override
    public String getAction()
    {
        return action;
    }

    @Nullable
    @Override
    public Bundle getExtras()
    {
        return extras;
    }
}
