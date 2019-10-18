package edu.uwstout.p2pchat;



import static android.view.View.GONE;

import androidx.recyclerview.widget.RecyclerView;

import edu.uwstout.p2pchat.databinding.ItemMessageBinding;

public class P2PMessageViewHolder extends RecyclerView.ViewHolder
{
    private final ItemMessageBinding binding;
    public P2PMessageViewHolder(ItemMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

    }
    public void bindData(P2PMessage message){
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
