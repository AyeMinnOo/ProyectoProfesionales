package com.youtube.sorcjc.proyectoprofesionales.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<Message> messages;
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messages = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.setMessage(message.getMessage());
        holder.setTime(message.getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addAll(@NonNull ArrayList<Message> messages) {
        if (messages == null)
            throw new NullPointerException("The message array cannot be null.");

        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void addItem(Message message) {
        if (message == null)
            throw new NullPointerException("The message cannot be null.");

        this.messages.add(message);
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        private TextView tvTime;

        public MessageViewHolder(View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }

        public void setMessage(String message){
            tvMessage.setText(Html.fromHtml(message + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")); // 10 spaces
        }

        public void setTime(String time) {
            tvTime.setText(time);
        }

    }

}
