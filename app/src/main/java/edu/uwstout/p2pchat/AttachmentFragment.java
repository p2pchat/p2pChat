package edu.uwstout.p2pchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uwstout.p2pchat.databinding.FragmentAttachmentBinding;

public class AttachmentFragment extends Fragment
{
    private FragmentAttachmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        binding = FragmentAttachmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

}
