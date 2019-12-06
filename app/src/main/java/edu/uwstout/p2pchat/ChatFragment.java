package edu.uwstout.p2pchat;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Date;
import java.util.List;

import edu.uwstout.p2pchat.databinding.FragmentChatBinding;
import edu.uwstout.p2pchat.room.Message;

/**
 * Fragment used for sending and receiving messages to/from peers
 */
public class ChatFragment extends Fragment
{
    private FragmentChatBinding binding;
    private LiveData<List<Message>> liveData;
    private P2PMessageAdapter messageAdapter;



    /**
     * Inflates layout xml and configures event listeners
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

        liveData = viewModel.getMessages(address);

        messageAdapter = new P2PMessageAdapter(liveData.getValue());
        binding.messagesRecyclerView.setAdapter(messageAdapter);

        liveData.observeForever(new Observer<List<Message>>() {
            /**
             * Gets called whenever a message associated with the current peer is added to the
             * database
             * @param messages
             */
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter = new P2PMessageAdapter(messages);
                binding.messagesRecyclerView.swapAdapter(messageAdapter, true);
                binding.messagesRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        final Context context = this.getContext();
        binding.sendButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Gets called whenever the send button is pressed
             * @param view
             */
            @Override
            public void onClick(final View view)
            {
                String text = binding.textInput.getText().toString();

                if(text.equals("")) return;

                Date timestamp = new Date();

                viewModel.insertMessage(address, timestamp, true, "text/message", text);

                binding.textInput.setText("");

                //A new InMemoryFile created with the text.
                InMemoryFile file = new InMemoryFile(text);

                //Send an inMemoryFile containing the current text message.
                WifiDirect.getInstance(context).sendInMemoryFile(file);

            }
        });
        binding.attachmentButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Gets called when the add attachment button is pressed
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
