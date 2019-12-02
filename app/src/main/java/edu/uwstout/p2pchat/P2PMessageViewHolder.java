package edu.uwstout.p2pchat;


import android.widget.RelativeLayout;

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
     * Takes in a message and displays text
     * @param message message object
     */
    void bindData(Message message)
    {

        binding.setTextOutput(message.content);
        binding.setTimestamp(message.timestamp.toString());
        binding.executePendingBindings();
    }

    /**
     * Takes in a boolean and determines alignment
     * @param sent sent boolean
     */
    void bindAlignment(boolean sent){
        RelativeLayout.LayoutParams lp1 =
                (RelativeLayout.LayoutParams) binding.chatMsgText.getLayoutParams();
        RelativeLayout.LayoutParams lp2 =
                (RelativeLayout.LayoutParams) binding.chatMsgTimestamp.getLayoutParams();
        if(sent)
        {
            lp1.addRule(RelativeLayout.ALIGN_PARENT_END);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
        }
        else
        {
            lp1.addRule(RelativeLayout.ALIGN_PARENT_START);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        binding.chatMsgText.setLayoutParams(lp1);
        binding.chatMsgTimestamp.setLayoutParams(lp2);


    }


}
