package edu.uwstout.p2pchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.LinkedList;


/**
 * This class here is responsible putting messy code into
 * adjustable readable methods and code.
 */
class SettingNode
{
    /**
     * The last theme color will be assigned this.
     */
    private final int colorSelected = Color.rgb(220, 220, 220);

    /**
     * Any other theme colors will be assigned this.
     */
    private final int defaultColor = Color.rgb(255, 255, 255);

    /**
     * All parentviews will be assigned this.
     */
    private TextView parentView[] = new TextView[4];

    /**
     * All subviews of MyInfo will be assigned this.
     */
    private LinkedList<TextView> childView1 = new LinkedList<>();
    /**
     * All subviews of theme color will be assigned here.
     */
    private LinkedList<TextView> childView2 = new LinkedList<>();

    /**
     * All subviews of nickname shall be assigned here. Only 0 will be assigned
     * here.
     */
    private LinkedList<TextView> childView3 = new LinkedList<>();

    /**
     * All subviews of help will be assigned here.
     */
    private LinkedList<TextView> childView4 = new LinkedList<>();
    //MAYBE
    FragmentManager manager;
    /**
     * To save theme color, will be done with this variable.
     */
    private Save saveInfo;
    /**
     * Name of each cell of expandible list view will be in here.
     */
    private String[] parentNames = {
            "My Device Info", "Theme Color: Light", "Set Device Nickname",
            "Help"
    };
    private String[][] childNames;
    private Activity mActivity;

    /**
     * The context will be stored in here.
     */
    private Context context;


    /**
     * Constructor for the setting nodes.
     *
     * @param context
     *         context needed for making other views.
     * @param childNames
     *         needed to use
     */
    public SettingNode(final Context context, final String[][] childNames, final Activity activity,
            final FragmentManager manager)
    {
        this.childNames = childNames;
        this.context = context;
        saveInfo = new Save(this.context);
        mActivity = activity;
        this.manager = manager;

        for (int general = 0; general < 4; general++)
        {
            //Assigns textviews to parent views.
            parentView[general] = createTextView(parentNames[general], true);

            //Assigns childviews textviews.
            for (int specific = 0; specific < childNames[general].length; specific++)
            {
                TextView view = createTextView(childNames[general][specific], false);

                //Determines what view gets what.
                switch (general)
                {
                    case 0:
                        childView1.add(view);
                        break;
                    case 1:
                        childView2.add(view);
                        break;
                    case 2:
                        childView3.add(view);
                        break;
                    case 3:
                        childView4.add(view);
                        break;
                }
            }
        }

        //Sets actionlisteners for theme color and nickname parents.
        setNickNameActionListener();
        setThemeColorListener();

        //Changes both parent
        changeTheme(saveInfo.getThemeColorThatShallBeSelected());


    }

    /**
     * Gets the textview of the child.
     * Indexs at childView[x] shall be
     * parent = x - 1
     */
    public TextView getView(int parent, int index)
    {
        switch (parent)
        {
            case 0:
                return childView1.get(index);

            case 1:
                return childView2.get(index);

            case 2:
                return childView3.get(index);

            case 3:
                return childView4.get(index);

        }
        return null;
    }

    /**
     * Gets the view from the parent.
     */
    public TextView getView(final int parent)
    {
        return parentView[parent];
    }


    /**
     * Clears all selected background colors
     * under theme color.(all childs).
     */
    private void clearBackgroundColor()
    {
        int size = childView2.size();
        for (int i = 0; i < size; i++)
        {
            childView2.get(i).setBackgroundColor(defaultColor);

        }
    }

    /**
     * Inserts the nickname into the list.
     */
    public void insertNickName(String name)
    {
        childView3.add(createTextView(name, false));
    }

    /**
     * Gets the child length from the parent.
     */
    public int getChildLength(int parent)
    {

        switch (parent)
        {
            case 0:
                return childView1.size();

            case 1:
                return childView2.size();

            case 2:
                return childView3.size();

            case 3:
                return childView4.size();

        }
        return 0;
    }

    /**
     * Creates textview. Support to be quicker and easier for
     * the programmer.
     */
    private TextView createTextView(final String text, final boolean isParent)
    {

        TextView view = new TextView(context);
        view.setText(text);
        view.setTextColor(Color.BLUE);

        //Decides if parent. Text size for parent is larger than child.
        if (isParent)
        {
            view.setTextSize(35);
        }
        else
        {
            view.setTextSize(25);
        }
        return view;

    }


    /**
     * Sets the theme color listeners.
     */
    public void setThemeColorListener()
    {
        //Children view of theme colors.
        final String THEMECHOICE[] =
                {"Theme Color: Light", "Theme Color: Majestic", "Theme Color: Dark"};

        //Theme color: light aka default selection.
        childView2.get(0).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeTheme(0);
                saveInfo.save(0);
                Log.i("Theme color changed to:",
                        THEMECHOICE[saveInfo.getThemeColorThatShallBeSelected()]);
            }
        });

        //Theme color: majestic.
        childView2.get(1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeTheme(1);
                saveInfo.save(1);
                Log.i("Theme color changed to:",
                        THEMECHOICE[saveInfo.getThemeColorThatShallBeSelected()]);
            }
        });


        //Changes the theme color for dark.
        childView2.get(2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeTheme(2);
                saveInfo.save(2);
                Log.i("Theme Color Changed to:",
                        THEMECHOICE[saveInfo.getThemeColorThatShallBeSelected()]);
            }
        });
    }

    /**
     * Sets the nick name action.
     */
    private void setNickNameActionListener()
    {


        //Creates the onclick listener for when the set nickname
        // tab get clicked.
        parentView[2].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //TODO change fragments here.
                Fragment currentFragment =
                        (Fragment) manager.findFragmentById(R.id.mainNavHostFragment);

                NavController nav = Navigation.findNavController(mActivity,
                        R.id.mainNavHostFragment);

                nav.navigate(R.id.toNickNameFragment);


            }
        });
    }


    /**
     * Shall be used.
     *
     * @param option
     *         0-2 are the options only allowed.
     */
    private void changeTheme(final int option)
    {
        final String THEMECOLOR[] =
                {"Theme Color: Light", "Theme Color: Majestic", "Theme Color: Dark"};

        switch (option)
        {
            case 0:
                parentView[1].setText(THEMECOLOR[0]);
                clearBackgroundColor();
                childView2.get(0).setBackgroundColor(colorSelected);
                break;
            case 1:
                parentView[1].setText(THEMECOLOR[1]);
                clearBackgroundColor();
                childView2.get(1).setBackgroundColor(colorSelected);
                break;
            case 2:
                parentView[1].setText(THEMECOLOR[2]);
                clearBackgroundColor();
                childView2.get(2).setBackgroundColor(colorSelected);
                break;
        }
    }


    /**
     * Makes a toast for easier use.
     *
     * @param text
     *         message displayed.
     */
    private void toast(String text)
    {
        StringBuilder builder = new StringBuilder();
        builder.insert(0, "New ThemeColor: " + text);
        text = builder.toString();

        //Makes the
        Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * Gets the parent view.
     * @return
     */
    public TextView[] getParent()
    {
        return parentView;
    }



}
