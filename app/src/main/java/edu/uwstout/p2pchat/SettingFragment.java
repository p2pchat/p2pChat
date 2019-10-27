package edu.uwstout.p2pchat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A fragment that hosts the settings.
 */
public class SettingFragment extends Fragment
{


    /**
     * Default constructor. Nothing will be used in here.
     */
    public SettingFragment()
    {
        // Required empty public constructor
    }


    /**
     * Creates the view.
     *
     * @param inflater
     *         default things needed for the onCreateview.
     * @param container
     *         container needed.
     * @param savedInstanceState
     *         needed.
     * @return view inflated and manipulated to fragment_settings xml.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState)
    {
        Context context;


        // Creates the view.
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //Save context for shared preferences.
        context = view.getContext();


        //Gets the expandable list view from the findViewById.
        ExpandableListView m = view.findViewById(R.id.eList);

        try
        {
            //New Listview-adapter instance.
            ExpandableSettingsListViewAdapter adapter =
                    new ExpandableSettingsListViewAdapter(context, this.getActivity(),
                            this.getActivity().getSupportFragmentManager());

            //Sets the adapter for Expandable list view.
            m.setAdapter(adapter);
        }
        catch (NullPointerException error)
        {
            //If an error occurs log it into the console as a warning.
            Log.w("Null Pointer Issue",
                    "Null for getSupportFragmentManager() " +
                    "ExpandableSettingsListViewAdapter: " + error.getMessage());

            //Make a toast that something went wrong.
            Toast.makeText(context, "Error with ExpandableSettingsListViewAdapter, please report " +
                            "this as a bug. Sorry for your inconvience.",
                    Toast.LENGTH_SHORT).show();

        }


        // Inflate the layout for this fragment
        return view;
    }


}
