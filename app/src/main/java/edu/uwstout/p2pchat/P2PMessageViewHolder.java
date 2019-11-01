package edu.uwstout.p2pchat;


import static android.view.View.GONE;

import android.view.Gravity;

import androidx.recyclerview.widget.RecyclerView;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;

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
    void bindData(P2PMessage message)
    {
        if (message.getMessageType().equals("Image"))
        {
            binding.chatMsgTextView.setVisibility(GONE);
            binding.chatMsgTextView.setMaxHeight(0);

        }
        else
        {
            binding.chatMsgImageView.setVisibility(GONE);
            binding.chatMsgImageView.setMaxHeight(0);
        }
        if (message.getStatus().equals("end"))
        {
            binding.chatMsgLayout.setGravity(Gravity.END);
        }
        else
        {
            binding.chatMsgLayout.setGravity(Gravity.START);
        }
        binding.setTextOutput(message.getText());
        binding.setTimestamp(message.getTimestamp());
        binding.executePendingBindings();
    }
}
