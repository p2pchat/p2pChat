package edu.uwstout.p2pchat;



import static android.view.View.GONE;

import androidx.recyclerview.widget.RecyclerView;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;

class P2PMessageViewHolder extends RecyclerView.ViewHolder
{
    private final ItemMessageBinding binding;

    /**
     *
     * @param binding
     */
    P2PMessageViewHolder(ItemMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

    }

    /**
     *
     * @param message
     */
    void bindData(P2PMessage message){
        if(message.getMessageType().equals("Image")){
            binding.chatMsgTextView.setVisibility(GONE);

        }else{
            binding.chatMsgImageView.setVisibility(GONE);
        }
        binding.setStatus(message.getStatus());
        binding.setTextOutput(message.getTimestamp() + ":" + message.getText());
        binding.executePendingBindings();
    }
}
