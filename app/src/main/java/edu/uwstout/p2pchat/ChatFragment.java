package edu.uwstout.p2pchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import edu.uwstout.p2pchat.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment
{
    private static FragmentChatBinding binding;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
         binding = FragmentChatBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }


    public static void ButtonClickListener()
    {
        binding.getInputText();

    }

}
