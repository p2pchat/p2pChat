package edu.uwstout.p2pchat;

import android.app.Application;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

public class TestNickNameModal extends NickNameModal
{

    public TestNickNameModal(Context context, String macaddress)
    {
        super(context, macaddress);
    }

    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}