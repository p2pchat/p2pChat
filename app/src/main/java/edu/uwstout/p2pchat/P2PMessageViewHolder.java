package edu.uwstout.p2pchat;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;
import edu.uwstout.p2pchat.room.Message;

class P2PMessageViewHolder extends RecyclerView.ViewHolder
{
    private final ItemMessageBinding binding;

    /**
     *
     */
    P2PMessageViewHolder(ItemMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

    }

    /**
     *
     */
    void bindData(Message message)
    {
        if(message.sent)
        {
            binding.chatMsgTextSent.setVisibility(View.GONE);
        }
        else
        {
            binding.chatMsgReceived.setVisibility(View.GONE);
        }
        binding.setTextOutput(message.content);
        binding.setTimestamp(message.timestamp.toString());
        binding.executePendingBindings();
    }
}
