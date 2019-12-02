package edu.uwstout.p2pchat;


import static android.view.View.GONE;

import android.view.Gravity;

import androidx.recyclerview.widget.RecyclerView;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;
import edu.uwstout.p2pchat.room.Message;

/**
 * View holder for messages in the chat history list
 */
class P2PMessageViewHolder extends RecyclerView.ViewHolder
{
    private final ItemMessageBinding binding;

    /**
     * Constructor that initializes local properties
     */
    P2PMessageViewHolder(ItemMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

    }

    /**
     * Sets the data displayed by this item.
     */
    void bindData(Message message)
    {
        if (message.mimeType.contains("image"))
        {
            binding.chatMsgTextView.setVisibility(GONE);
            binding.chatMsgTextView.setMaxHeight(0);

        }
        else
        {
            binding.chatMsgImageView.setVisibility(GONE);
            binding.chatMsgImageView.setMaxHeight(0);
        }
        if (message.sent)
        {
            binding.chatMsgLayout.setGravity(Gravity.END);
        }
        else
        {
            binding.chatMsgLayout.setGravity(Gravity.START);
        }
        binding.setTextOutput(message.content);
        binding.setTimestamp(message.timestamp.toString());
        binding.executePendingBindings();
    }
}
