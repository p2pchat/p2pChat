package edu.uwstout.p2pchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;


/**
 * A expandable-list-view-adapter for the Settings Fragment.
 * ------------INSTRUCTIONS TO ADD A SETTING--------.
 * 1. ADD NEW NAME IN: "parentNames".
 * 2. ADD ANY CHILD IN "childNames".
 * 3. ADD ANY ON CLICK LISTENERS IN: "getGroupView()" method for any;
 *      actions you want for the parentName tab.
 * 4. ADD ANY ON CLICK LISTENERS IN: "getChildView()" method for any;
 *      actions you want for the subviews of the parent.
 * 5. DONE: EVERYTHING ELSE IS TAKEN CARE FOR YOU.
 * NOTE: BY getGroupView() is what you see. When you click on the label, it will expand the list.
 * I call the expanded list, the subviews.
 * @author Nick Zolondek
 */
public class ExpandableSettingsListViewAdapter extends BaseExpandableListAdapter
{


    /**
     * The context that will be used for initializing and changing
     * fragments.
     */
    private Context classContext;

    /**
     * Created in the text views.
     */
    private int parentSize;

    /**
     * Stores all child Sizes.
     * Helps with time complexity.
     */
    private int[] childSize;

    /**
     * Stores all views. Both parent and child views.
     */
    private Map<String, TextView> nameViews;

    /**
     * Activity that will be
     */
    private Activity classActivity;

    /**
     * Background color selected.
     */
    private final int colorSelected = Color.rgb(220, 220, 220);

    /**
     * Background color for non selected.
     */
    private final int defaultColor = Color.rgb(255, 255, 255);


    /**
     * Constructor for building an expandable list view adapter.
     * Initializes the name arrays into views.
     *
     * @param context
     *         the context of the class.
     */
    public ExpandableSettingsListViewAdapter(final Context context, Activity currentActivity)
    {
        // Assign the context.
        classContext = context;

        //a map that uses Hashmap. Stores all views.
        nameViews = new HashMap<>();

        // Used for the navigation pane for the settings, help.
        //For code, go down to getGroupView.
        this.classActivity = currentActivity;


        //Customize info here.
        final int PARENT_TEXT_SIZE = 38;
        final int CHILD_TEXT_SIZE = 30;

        //Text Colors here.
        final int textColorChild = Color.BLUE;
        final int textColorParent = Color.GREEN;


        //TODO Add name here.
        //Assign all parent names the following.
        String[] parentNames = {
                "My Device Info", "Theme Color: Light", "Set Device Nickname", "Help"
        };

        //Todo Add empty set here(for adding another setting).
        //Child View names.
        String[][] childNames = {
                {
                        "Creator: ", "Device Address: ", "Device Name: ",
                        "Primary Device Type: ", "Secondary Device Type: ", "Status: "
                },
                {"Light", "Majestic", "Dark"}, {}, {}
        };



        //Size of parent.
        parentSize = childNames.length;
        childSize = new int[parentSize];

        //Get the saved preferences for color.
        Save save = new Save(classContext);
        int selectionForThemeColor = save.getThemeColorThatShallBeSelected();

        //Assign lengths to the child size.
        for (int parent = 0; parent < childNames.length; parent++)
        {
            childSize[parent] = childNames[parent].length;
        }


        //WARNING: Do not mess down below.
        //Assign all text from the parent
        for (int parent = 0; parent < childNames.length; parent++)
        {
            //Create text view for parent.
            TextView tempView = new TextView(classContext);

            //ANYTHING ADDITIONAL GOES HERE.
            tempView.setText(parentNames[parent]);
            tempView.setTextSize(PARENT_TEXT_SIZE);

            //Put tempView into the map of Textviews.
            nameViews.put(getKeyParent(parent), tempView);

            //Creates the view for the child.
            for (int child = 0; child < childNames[parent].length; child++)
            {
                // clears textview.
                tempView = new TextView(classContext);

                // ANYTHING ADDITONAL GOES DOWN HERE.
                tempView.setText(childNames[parent][child]);
                tempView.setTextSize(CHILD_TEXT_SIZE);


                nameViews.put(getKeyChild(parent, child), tempView);
            }
        }

        //sets the theme color from the start.
        setThemeColor(selectionForThemeColor);

    }

    /**
     * Gets count of parent nodes.
     * @return length of parents.
     */
    @Override
    public int getGroupCount()
    {
        //TODO Change
        //By default
        return parentSize;
    }

    /**
     * Gets the count of each child under a certain parent.
     *
     * @param parent
     *         specify parent position.
     * @return number of children under a certain view.
     */
    @Override
    public int getChildrenCount(final int parent)
    {

        //Returns the child size.
        return childSize[parent];
    }

    /**
     * Returns the text displayed.
     * @param parent
     *         parent index.
     * @return string of the parents.
     */
    @Override
    public Object getGroup(final int parent)
    {
        //Gets key from the parent index.
        String key = getKeyParent(parent);

        //REturns the text of the textview.
        return nameViews.get(key).getText().toString();
    }

    /**
     * Get the text from a child.
     *
     * @param parent
     *         parent index.
     * @param child
     *         child index;
     * @return the text of the child.
     */
    @Override
    public Object getChild(final int parent, final int child)
    {
        String key = getKeyChild(parent, child);

        //RECALL: child is from index 1 - x.
        return nameViews.get(key).getText().toString();

    }

    /**
     * Gets the group id of the parent.
     *
     * @param parentID
     *         position of parent.
     * @return parent location from i.
     */
    @Override
    public long getGroupId(final int parentID)
    {

        return parentID;
    }

    /**
     * Get the position of child given the parent index.
     *
     * @param generalSetting
     *         parent index
     * @param specificSetting
     *         specific child index to look at.
     * @return the child position.
     */
    @Override
    public long getChildId(final int generalSetting, final int specificSetting)
    {
        return specificSetting;
    }

    /**
     * Will be false.
     * Required by the BaseExpandableListAdapter class.
     *
     * @return false.
     */
    @Override
    public boolean hasStableIds()
    {
        return false;
    }


    /**
     * Returns the view of the parent.
     *
     * @param groupPosition
     *         parent position.
     * @param b
     *         not applicable.
     * @param view
     *         not applicable
     * @param viewGroup
     *         not applicable.
     * @return view of the parent.
     */
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup)
    {

        String key = getKeyParent(groupPosition);
        TextView currentView = nameViews.get(key);

        // Add an action listener for parent views here.
        switch (groupPosition) {

            case 2:
                //settings page.
                currentView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        NavController nav = Navigation.findNavController(classActivity,
                                R.id.mainNavHostFragment);

                        nav.navigate(R.id.toNickNameFragment);
                    }
                });
                break;
            case 3:
                //TODO Help page goes here.
                break;
            case 4:
                //TODO Clear all database goes here.
                break;

        }

        return nameViews.get(key);
    }

    /**
     * Gets the child view.
     * Add all onclick listeners for subviews of the expandable views here.
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
    public View getChildView(int generalPos, final int specificPos, boolean b, View view,
            ViewGroup viewGroup)
    {
        //Get the current view of the child.
        String key = getKeyChild(generalPos, specificPos);
        TextView view2 = nameViews.get(key);

        //Theme color selected.
        if (generalPos == 1)
        {
            //For all theme buttons pressed.
            view2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //Changes theme color and background.
                    setThemeColor(specificPos);
                }
            });
        }
        //Returns the nameView.
        return view2;
    }


    /**
     * Not applicable for this case, but required to have.
     *
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


    /**
     * Uses StringBuilder for faster String manipulation.
     * Gets the key for a child.
     * Will convert a key into the following.
     * parent-child. Example: 1-1.
     *
     * @param parent
     *         parent index.
     * @param child
     *         child index.
     * @return key.
     */
    private String getKeyChild(int parent, int child)
    {
        StringBuilder key = new StringBuilder();
        key.append(parent);
        key.append("-");
        key.append(child);
        return key.toString();
    }

    /**
     * Converts an integer into a key.
     * @param parent
     *         parent index.
     * @return returns key of the map.
     */
    private String getKeyParent(int parent)
    {
        //String builder is quicker than string concatenation.

        //Create a string builder.
        StringBuilder builder = new StringBuilder();

        //Appends to String builder.
        builder.append(parent);
        return builder.toString();
    }

    /**
     * Changes the theme color when a theme color is pressed.
     * @param themeColor 0-2 for theme color options.
     */
    private void setThemeColor(int themeColor)
    {
        //Create a new save object.
        Save save = new Save(classContext);
        save.getThemeColorThatShallBeSelected();

        //Change the previous theme color. Change  background color to not selected.
        int previousTheme = save.getThemeColorThatShallBeSelected();
        String previousSelectionKey = getKeyChild(1, previousTheme);
        TextView prevView = nameViews.get(previousSelectionKey);
        prevView.setBackgroundColor(defaultColor);

        //Gets the view for the parent of "theme color" tab.
        String parentKey = getKeyParent(1);
        TextView view = nameViews.get(parentKey);

        //Gets the newly selected textview and changes the color to selected.
        String currentThemeKey = getKeyChild(1, themeColor);
        TextView currentTextTheme = nameViews.get(currentThemeKey);
        currentTextTheme.setBackgroundColor(colorSelected);

        // Creates a new stringbuilder for creating parent title for theme color.
        StringBuilder parentBuilder = new StringBuilder();
        parentBuilder.append("Theme Color: ");

        // Add title seperately.
        switch (themeColor) {
            case 0:
                parentBuilder.append("Light");
                break;
            case 1:
                parentBuilder.append("Majestic");
                break;
            case 2:
                parentBuilder.append("Dark");
                break;
        }


        //Change the text from parent.
        view.setText(parentBuilder.toString());

        //Update the shared preferences.
        save.save(themeColor);
    }


}
