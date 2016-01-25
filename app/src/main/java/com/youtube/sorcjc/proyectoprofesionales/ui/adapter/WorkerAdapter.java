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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Worker;
import com.youtube.sorcjc.proyectoprofesionales.ui.ProfileActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.TalkActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.custom.CircleTransform;

import java.util.ArrayList;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {

    private ArrayList<Worker> workers;
    private Context context;

    public WorkerAdapter(Context context) {
        this.context = context;
        this.workers = new ArrayList<>();
    }

    @Override
    public WorkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new WorkerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkerViewHolder holder, int position) {
        Worker worker = workers.get(position);

        holder.setName(worker.getName());
        holder.setImage(worker.getUrlPhoto());
        holder.setImageClick(worker.getPid());
        holder.setDescription(worker.getCatstr());
        holder.setChatClick(worker.getUid(), worker.getCatstr());
        holder.setDate("");
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    public void addAll(@NonNull ArrayList<Worker> workers) {
        if (workers == null)
            throw new NullPointerException("The results cannot be null.");

        this.workers.addAll(workers);
        notifyDataSetChanged();
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvDate;

        private ImageView ivOpenChat;
        private LinearLayout contact_info;

        public WorkerViewHolder(View itemView) {
            super(itemView);

            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            ivOpenChat = (ImageView) itemView.findViewById(R.id.ivOpenChat);
            contact_info = (LinearLayout) itemView.findViewById(R.id.contact_info);
        }

        public void setName(String name){
            tvName.setText(name);
        }

        public void setImage(String urlImage) {
            // Get image from URL
            Picasso.with(context)
                    .load(urlImage)
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .transform(new CircleTransform())
                    .into(ivPhoto);
        }

        public void setImageClick(final String pid) {
            // Set event click
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ProfileActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("pid", pid);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }
            });
        }

        public void setChatClick(final String uid, final String catstr) {
            // Set event click for the message icon and contact info
            ChatClickListener chatClickListener = new ChatClickListener(uid, catstr);

            ivOpenChat.setOnClickListener(chatClickListener);
            contact_info.setOnClickListener(chatClickListener);
        }

        class ChatClickListener implements View.OnClickListener {
            private final String uid;
            private final String catstr;

            public ChatClickListener(String uid, String catstr) {
                this.uid = uid;
                this.catstr = catstr;
            }

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), TalkActivity.class);
                Bundle b = new Bundle();
                b.putString("uid", uid);
                b.putString("catstr", catstr);
                i.putExtras(b);
                view.getContext().startActivity(i);
            }
        }

        public void setDescription(String description) {
            tvDescription.setText(description);
        }

        public void setDate(String date) {
            tvDate.setText(date);
        }

    }

}
