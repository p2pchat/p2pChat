package edu.uwstout.p2pchat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;
import edu.uwstout.p2pchat.room.Message;

/**
 * RecyclerView adapter used by the Chat Fragment
 */
public class P2PMessageAdapter extends RecyclerView.Adapter<P2PMessageViewHolder>
{

    private List<Message> messageList;

    /**
     * Constructor that initializes the messageList property
     */
    P2PMessageAdapter(List<Message> messageList)
    {
        this.messageList = messageList;
    }

    /**
     * Inflates message views to be added to the RecyclerView
     */
    @NonNull
    @Override
    public P2PMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMessageBinding binding = ItemMessageBinding.inflate(layoutInflater, parent, false);
        return new P2PMessageViewHolder(binding);

    }

    /**
     * Sets the data to be displayed by a particular view
     */
    @Override
    public void onBindViewHolder(@NonNull P2PMessageViewHolder holder, int position)
    {
        Message message = this.messageList.get(position);

        holder.bindData(message);
    }

    /**
     * Returns the number of views
     */
    @Override
    public int getItemCount()
    {
        if (messageList == null)
        {
            messageList = new ArrayList<Message>();
        }
        return messageList.size();
    }


}
