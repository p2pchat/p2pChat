package edu.uwstout.p2pchat;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * A class for saving and loading theme option.
 * Relatable only for HomeFragment.
 *
 */
public class Save
{
    /**
     * The default name that will be used as a key to store information.
     */
    private final static String KEYPACKAGE = "edu.uwstout.p2pchat";

    /**
     * The key that will retrieve and send the title of the expandable
     * list item for theme color.
     */
    private final static String THEME = "themeString";

    /**
     * The key needed to get and save the integer for theme color options.
     */
    private final static String THEMEPOSITION = "themeColorOption";

    /**
     * The sharedpreferences variable used
     */
    private SharedPreferences mSharedPreferences;

    /**
     * Creates a saving object.
     */
    public Save(final Context context)
    {
        mSharedPreferences = context.getSharedPreferences(KEYPACKAGE, 0);
    }


    /**
     * Saves the themeColor textview that needs to be selected.
     * @param themeColor color position from the child lists of
     *                   expandable list views.
     */
    public void save(final int themeColor)
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(THEMEPOSITION, themeColor);
        editor.apply();
    }


    /**
     * Gets the theme color int option that should be selected.
     *
     * @return theme color tab.
     */
    public int getThemeColorThatShallBeSelected()
    {
        return mSharedPreferences.getInt(THEMEPOSITION, 0);
    }

}
