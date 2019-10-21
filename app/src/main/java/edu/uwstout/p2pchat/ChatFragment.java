package edu.uwstout.p2pchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment
{
    private static FragmentChatBinding binding;
    private final List<P2PMessage> messages = new ArrayList<>();
    private P2PMessageAdapter messageAdapter;

    /**
     *
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        binding = FragmentChatBinding.inflate(inflater, container, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.messagesRecyclerView.setLayoutManager(linearLayoutManager);

        messageAdapter = new P2PMessageAdapter(messages);
        binding.messagesRecyclerView.setAdapter(messageAdapter);

        binding.sendButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             *
             * @param view
             */
            @Override
            public void onClick(View view)
            {
                String text = binding.textInput.getText().toString();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                P2PMessage message = new P2PMessage(text, timestamp.toString(),
                        "end", "text");
                messages.add(message);
                int newMessagePosition = messages.size() - 1;

                messageAdapter.notifyItemInserted(newMessagePosition);

                binding.messagesRecyclerView.scrollToPosition(newMessagePosition);
                binding.textInput.setText("");
            }
        });


        return binding.getRoot();
    }

}
