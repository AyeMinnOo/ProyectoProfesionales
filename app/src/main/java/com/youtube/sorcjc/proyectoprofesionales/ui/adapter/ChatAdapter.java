package com.youtube.sorcjc.proyectoprofesionales.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Chat;
import com.youtube.sorcjc.proyectoprofesionales.ui.TalkActivity;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private ArrayList<Chat> chats;
    private Context context;

    public ChatAdapter(Context context) {
        this.context = context;
        this.chats = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Chat chat = chats.get(position);

        holder.setName(chat.getName());
        holder.setImage(chat.getUrlPhoto());
        holder.setDescription(chat.getDescription());
        holder.setDate(chat.getActivity());
        holder.setClickEvent(chat.getUid());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void addAll(@NonNull ArrayList<Chat> chats) {
        if (chats == null)
            throw new NullPointerException("The results cannot be null.");

        this.chats.addAll(chats);
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvDate;

        private ImageView ivOpenChat;

        public MessageViewHolder(View itemView) {
            super(itemView);

            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            ivOpenChat = (ImageView) itemView.findViewById(R.id.ivOpenChat);
        }

        public void setName(String name){
            tvName.setText(name);
        }

        public void setImage(String urlImage) {
            Picasso.with(context)
                    .load(urlImage)
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(ivPhoto);
        }

        public void setDescription(String description) {
            tvDescription.setText(description);
        }

        public void setDate(String date) {
            tvDate.setText(date);
        }

        public void setClickEvent(final String uid) {
            ivOpenChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), TalkActivity.class);
                    Bundle b = new Bundle();
                    b.putString("uid", uid);
                    i.putExtras(b);
                    view.getContext().startActivity(i);
                }
            });
        }

    }

}
