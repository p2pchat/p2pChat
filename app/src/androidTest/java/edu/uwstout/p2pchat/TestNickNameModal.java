package edu.uwstout.p2pchat;

import android.app.Application;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.uwstout.p2pchat.room.Peer;
import edu.uwstout.p2pchat.testing.MockViewModel;

/**
 * Creates a Test version of the NickNameModal that uses a MockViewModel
 */
public class TestNickNameModal extends NickNameModal {
    /**
     * Pass-through constructor
     * @param context view context where the modal is being displayed
     * @param macAddress The mac address of the peer whose nickname we are changing
     */
    public TestNickNameModal(Context context, String macAddress) {
        super(context, macAddress);
    }

    /**
     * Returns a MockViewModel
     * @param app Application context
     * @return a Mock of the database view model
     */
    @Override
    public ViewModel getViewModel(Application app) {
        return new MockViewModel(app);
    }
}