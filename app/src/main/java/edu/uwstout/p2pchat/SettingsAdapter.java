package edu.uwstout.p2pchat;

import android.content.res.Resources;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import edu.uwstout.p2pchat.SettingActionListeners.ChangeThemeColorListener;
import edu.uwstout.p2pchat.SettingActionListeners.ConfirmRemoveDatabaseListener;
import edu.uwstout.p2pchat.SettingActionListeners.HelpPageListener;
import edu.uwstout.p2pchat.SettingActionListeners.NickNameListener;

/**
 * Responsible for giving Expandiblelist view under settings
 * actions and responsibilities.
 */
public class SettingsAdapter extends BaseExpandableListAdapter
{
    /**
     * A global fragment.
     */
    private Fragment fragment;

    /**
     * Build a Settings Adapter object.
     * @param fragment fragment.
     */
    public SettingsAdapter(Fragment fragment)
    {

        this.fragment = fragment;
    }

    /**
     * Get group count.
     * @return number of parent names.
     */
    @Override
    public int getGroupCount()
    {
        return getParentsArray().length;
    }

    /**
     * Get number of children.
     * @param parent index of parent.
     * @return number of children count.
     */
    @Override
    public int getChildrenCount(int parent)
    {
        return getChildsArray(parent).length;
    }

    /**
     * Get String of the object.
     * @param parent parent index.
     * @return string of the parent.
     */
    @Override
    public Object getGroup(int parent)
    {
        return getParentsArray()[parent];
    }

    /**
     * Get Child Object which is a string.
     * @param parent parent index.
     * @param child specific child.
     * @return child string.
     */
    @Override
    public Object getChild(int parent, int child)
    {
        return getChildsArray(parent)[child];
    }

    /**
     * Get group id.
     * @param parent parent index.
     * @return parent.
     */
    @Override
    public long getGroupId(int parent)
    {
        return parent;
    }

    /**
     * Get Child ID.
     * @param parent parent index.
     * @param child child index.
     * @return child.
     */
    @Override
    public long getChildId(int parent, int child)
    {
        return child;
    }

    /**
     * It does not have stable ids.
     * @return false.
     */
    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    /**
     * Get the group view.
     * @param parent parent index.
     * @param b if it is expanded.
     * @param view view being manipulated.
     * @param viewGroup rows that are visible.
     * @return new view.
     */
    @Override
    public View getGroupView(int parent, boolean b, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.settings_item, viewGroup, false);

        //Get the textview applicable for this group.
        TextView textView = (TextView) view.findViewById(R.id.parentText);
        textView.setText(getParentsArray()[parent]);

        //SEt visible; It is invisible by default.
        textView.setVisibility(View.VISIBLE);


        if (parent == 1)
        {
            Save save = new Save(fragment);
            view.setTag(0);
            textView.setText(save.getTitle());
        }

        //Clear All database.
        if (parent == 2)
        {
            //Assign clearing database on view listener.
            view.setOnClickListener(new ConfirmRemoveDatabaseListener(fragment));
        }

        //Rename.
        if (parent == 3) {
            view.setOnClickListener(new NickNameListener(fragment));
        }

        // Help page.
        if (parent == 4) {
            view.setTag(10);
            view.setOnClickListener(new HelpPageListener());
        }

        return view;
    }

    /**
     * Get the child view. Creates, tag and adds action listeners to some views.
     * @param parent parent index.
     * @param child child index.
     * @param b boolean value.
     * @param view current view.
     * @param viewGroup view group.
     * @return new view.
     */
    @Override
    public View getChildView(int parent, int child, boolean b, View view, ViewGroup viewGroup)
    {
        // Access the save class.
        Save save = new Save(fragment);

        //Inflate each layout.
        LayoutInflater inflater = fragment.getActivity().getLayoutInflater();

        //Get the inflated view from layout inflator.
        View view2 = inflater.inflate(R.layout.settings_item, viewGroup, false);

        //Find the text view by id.
        TextView textView = view2.findViewById(R.id.childText);

        //Set visible, by default it is invisible.
        textView.setVisibility(View.VISIBLE);


        //Change text corresponding to what parent is.
        if (parent == 0)
        {
            textView.setText(getMyDeviceInfo()[child]);
        }
        else
        {
            //Assign names like normal.
            textView.setText(getChildsArray(parent)[child]);
        }



        // Set the background color for that specific textview.
        if (save.getThemeColorThatShallBeSelected() == child && parent == 1) {
            view2.setBackgroundColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.selectedBackgroundColor));
        }


        //Take care of special conditions.
        if (parent == 1)
        {
            //SEt tage for each of the child. USed for retrieving the child for
            view2.setTag(child + 1);

            //SEt the listener for each of the childs.
            view2.setOnClickListener(new ChangeThemeColorListener(fragment, viewGroup, child));
        }

        return view2;
    }

    /**
     * Determines if the child is selectable.
     * @param parent parent index.
     * @param child child index.
     * @return true.
     */
    @Override
    public boolean isChildSelectable(int parent, int child)
    {
        return true;
    }


    /**
     * Get the parents.
     * @return array of parent Text.
     */
    private String[] getParentsArray() {
        Resources resources = fragment.getResources();
        return resources.getStringArray(R.array.parentSettingsName);
    }

    /**
     * Get child array.
     * @param parent parent.
     * @return array of childs.
     */
    @NotNull
    private String[] getChildsArray(int parent)
    {
        // Get the resources to access the arrays of strings from string.xml.
        Resources resources = fragment.getResources();

        //Switch case for parents.
        switch (parent)
        {
            case 0:
                return resources.getStringArray(R.array.myDeviceInfoSettings);
            case 1:
                return resources.getStringArray(R.array.themeColor);
        }
        //If outside the two parent numbers, than want to return empty array.
        return new String[0];
    }


    /**
     * Get my device info and assign it to the listviews.
     * @return new strings of my device info.
     */
    private String[] getMyDeviceInfo()
    {
        //Get the WifiP2Pdevice and assign values to the list views.
        WifiP2pDevice myDevice = WifiDirect.getInstance(fragment.getContext()).getThisDevice();

        // Use this get set the info for each of the child arrays.
        String info[] = getChildsArray(0);

        //Set the string of labels here.
        info[0] = info[0] + " " + myDevice.deviceAddress;
        info[1] = info[1] + " " + myDevice.deviceName;
        info[2] = info[2] + " " + myDevice.primaryDeviceType;
        info[3] = info[3] + " " + myDevice.secondaryDeviceType;
        info[4] = info[4] + " " + myDevice.status;

        //Return recently updated info[] so it can list the strings.
        return info;
    }
}
