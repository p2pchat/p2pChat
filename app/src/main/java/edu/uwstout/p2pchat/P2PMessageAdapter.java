package edu.uwstout.p2pchat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;
import edu.uwstout.p2pchat.room.Message;

public class P2PMessageAdapter extends RecyclerView.Adapter<P2PMessageViewHolder>
{

    private List<Message> messageList;

    /**
     *
     */
    P2PMessageAdapter(List<Message> messageList)
    {
        this.messageList = messageList;
    }

    /**
     *
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
     *
     */
    @Override
    public void onBindViewHolder(@NonNull P2PMessageViewHolder holder, int position)
    {
        Message message = this.messageList.get(position);




        holder.bindAlignment(message.sent);
        holder.bindData(message);
    }

    /**
     *
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
