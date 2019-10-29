package edu.uwstout.p2pchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ExpandableSettingsListViewAdapter extends BaseExpandableListAdapter
{


    /**
     * Creates the setting node.
     * Class is responsible for intepretting the
     * data in a cleaner way.
     */
    private SettingNode views;

    /**
     * The context that will be used for initializing and changing
     * fragments.
     */
    private Context classContext;



    /**
     * Constructor for building an expandable list view adapter.
     * Initializes the name arrays into views.
     *
     * @param context
     *         the context of the class.
     */
    public ExpandableSettingsListViewAdapter(final Context context, Activity currentActivity,
            FragmentManager manager)
    {
        // Assign the context.
        classContext = context;




        //Assign all texts of childviews the following.
        String[][] childNames = {
                {
                        "Creator: ", "Device Address: ",
                        "Device Name: ",
                        "Primary Device Type: ", "Secondary Device Type: ", "Status: "
                }, {
                "Light",
                "Majestic", "Dark"
        }, {}, {}
        };


        views = new SettingNode(context, childNames, currentActivity, manager);



    }

    /**
     * Gets count of parent nodes.
     * @return
     */
    @Override
    public int getGroupCount()
    {
        return views.getParent().length;
    }

    /**
     * Gets the count of each child under a certain parent.
     * @param parent specify parent position.
     * @return number of children under a certain view.
     */
    @Override
    public int getChildrenCount(final int parent)
    {
        //Returns the child size.
        return views.getChildLength(parent);
    }

    /**
     * @param i parent index.
     * @return string of the parents.
     */
    @Override
    public Object getGroup(final int i)
    {
        return views.getParent()[i].getText().toString();
    }

    /**
     * Get the text from a child.
     * @param i parent index.
     * @param i1 child index;
     * @return the text of the child.
     */
    @Override
    public Object getChild(final int i, final int i1)
    {
        return views.getView(i, i1).getText().toString();
    }

    /**
     * Gets the group id of the parent.
     * @param i position of parent.
     * @return parent location from i.
     */
    @Override
    public long getGroupId(final int i)
    {

        return i;
    }

    /**
     * Get the position of child given the parent index.
     * @param generalSetting parent index
     * @param specificSetting specific child index to look at.
     * @return the child position.
     */
    @Override
    public long getChildId(final int generalSetting, final int specificSetting)
    {
        return specificSetting;
    }

    /**
     * Will be false.
     * @return false.
     */
    @Override
    public boolean hasStableIds()
    {
        return false;
    }


    /**
     * Returns the view of the parent.
     * @param groupPosition parent position.
     * @param b not applicable.
     * @param view not applicable
     * @param viewGroup not applicable.
     * @return view of the parent.
     */
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup)
    {
        TextView v[] = views.getParent();
        return v[groupPosition];
    }

    /**
     * Gets the child view.
     *
     * @param generalPos
     *         general position. Ex: Theme Color.
     * @param specificPos
     *         More specific settings. Subsettings of the general settings.
     * @param b
     *         boolean value.
     * @param view
     *         view
     * @param viewGroup
     *         view group.
     */
    @Override
    public View getChildView(int generalPos, int specificPos, boolean b, View view,
            ViewGroup viewGroup)
    {

         return views.getView(generalPos, specificPos);
    }

    /**
     * Not applicable for this case.
     * @param parent_index
     *         index of parent.
     * @param child_index
     *         index of child
     * @return false
     */
    @Override
    public boolean isChildSelectable(int parent_index, int child_index)
    {
        return false;
    }









}
