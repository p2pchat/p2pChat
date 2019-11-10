package edu.uwstout.p2pchat;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.databinding.FragmentChatBinding;
import edu.uwstout.p2pchat.room.Message;

public class ChatFragment extends Fragment
{
    private FragmentChatBinding binding;
    private List<Message> messages;
    private P2PMessageAdapter messageAdapter;

    /**
     *
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.messagesRecyclerView.setLayoutManager(linearLayoutManager);
        ViewModel viewModel = getViewModel(getActivity().getApplication());
        String address = getPeerMacAddress();
        messages = viewModel.getMessages(address).getValue(); // TODO: implement this correctly
        messageAdapter = new P2PMessageAdapter(messages);
        binding.messagesRecyclerView.setAdapter(messageAdapter);

        binding.sendButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             *
             * @param view
             */
            @Override
            public void onClick(final View view)
            {
                String text = binding.textInput.getText().toString();
                Date timestamp = new Date();
                viewModel.insertMessage(address, timestamp, true, "text/message", text);
                int newMessagePosition = messages.size() - 1;

                messageAdapter.notifyItemInserted(newMessagePosition);

                binding.messagesRecyclerView.scrollToPosition(newMessagePosition);
                binding.textInput.setText("");
            }
        });
        binding.attachmentButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             *
             * @param view
             */
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(),"attachment clicked",Toast.LENGTH_SHORT);
            }
        });


        return binding.getRoot();
    }

    /**
     * To be overrided for testing purposes
     * @param app Current application
     * @return Application's ViewModel
     */
    public ViewModel getViewModel(Application app) {
        return new ViewModel(app);
    }

    /**
     * To be overrided for testing purposes
     * @return Mac Address of peer currently connected to
     */
    public String getPeerMacAddress() {
        return ChatFragmentArgs.fromBundle(getArguments()).getAddress();
    }

}
