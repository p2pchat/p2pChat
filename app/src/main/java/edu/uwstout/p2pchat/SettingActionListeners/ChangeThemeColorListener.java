package edu.uwstout.p2pchat.SettingActionListeners;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import edu.uwstout.p2pchat.R;
import edu.uwstout.p2pchat.Save;

public class ChangeThemeColorListener implements View.OnClickListener
{
    /**
     * View group from the getGroupView.
     */
    private ViewGroup viewGroup;

    /**
     * Fragment currently in use.
     */
    private Fragment fragment;

    /**
     * Index that is being manipulated.
     */
    private int currentIndex;


    /**
     * Passes through all infomration required for changing the theme color.
     */
    public ChangeThemeColorListener(Fragment fragment, ViewGroup viewGroup, int child)
    {
        this.viewGroup = viewGroup;
        this.fragment = fragment;
        currentIndex = child;
    }

    /**
     * Take care of the on click listener here.
     *
     * @param view
     *         view that had been touched.
     */
    @Override
    public void onClick(View view)
    {
        // Retrieve parent Text View.
        TextView view1 = viewGroup.findViewWithTag(0).findViewById(R.id.parentText);

        // NAme of theme headers.
        String themeNames[] =
                fragment.getResources().getStringArray(R.array.themeColorTitle);

        // SEt the text.
        view1.setText(themeNames[currentIndex]);

        //Max id 1-4 is the range.
        final int MAXID = 4;

        // Go around Clearing all other background color for text views and set background color
        // For the on clicked View.
        for (int id = 1; id < MAXID; id++)
        {
            View view2 = viewGroup.findViewWithTag(id); //.findViewById(R.id.childText);

            //Clear All background colors.
            view2.setBackgroundColor(ContextCompat.getColor(fragment.getContext(),
                    R.color.defaultBackgroundColor));


            // If view that matches the id that is tagged, then set background color to
            //selected background color.
            if ((currentIndex + 1) == id)
            {
                view2.setBackgroundColor(ContextCompat.getColor(fragment.getContext()
                        , R.color.selectedBackgroundColor));

                view.setBackgroundColor(ContextCompat
                        .getColor(fragment.getContext(), R.color.selectedBackgroundColor));
            }
        }

        //Save the new selected item.
        Save save = new Save(fragment);

        //Update saved index.
        save.save(currentIndex);
    }
}
