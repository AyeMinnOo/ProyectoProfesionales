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
        View itemView;

        if (viewType == 1)
            itemView = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
        else
            itemView = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.setMessage(message.getContent());
        holder.setTime(message.getCreated());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).amISender(context.getApplicationContext()))
            return 1;
        return 0;
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

    public String getParentMid() {
        if (this.messages == null || this.messages.size() == 0)
            return "0";

        final int lastPosition = this.messages.size() - 1;
        return this.messages.get(lastPosition).getParentMid();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        private ImageView ivImagen;
        private TextView tvTime;

        public MessageViewHolder(View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }

        public void setMessage(String message) {
            int startImage = message.indexOf("[image]");

            if (startImage != -1) {
                // The message is an image
                int endImage = message.indexOf("[/image]");
                String urlImage = message.substring(7, endImage);
                ivImagen.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);

                // Get image from URL
                Picasso.with(context)
                        .load(urlImage)
                        .placeholder(R.drawable.ic_category_default)
                        .into(ivImagen);

            } else {
                // This is logically not necessary but fix a strange bug
                ivImagen.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);

                // The message is a simple text
                tvMessage.setText(Html.fromHtml(message + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")); // 10 spaces
            }

        }

        public void setTime(String time) {
            tvTime.setText(time);
        }

    }

}