package com.homesolution.app.ui.adapter;

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
import com.homesolution.app.domain.Worker;
import com.homesolution.app.ui.activity.ProfileActivity;
import com.homesolution.app.ui.custom.CircleTransform;

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
        holder.setEventClick(worker.getPid());
        holder.setDescription(worker.getCatstr());
        holder.hideDate();
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

    public void setAll(@NonNull ArrayList<Worker> workers) {
        if (workers == null)
            throw new NullPointerException("The results cannot be null.");

        this.workers = workers;
        notifyDataSetChanged();
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvDate;

        private LinearLayout layout_info;

        public WorkerViewHolder(View itemView) {
            super(itemView);

            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            layout_info = (LinearLayout) itemView.findViewById(R.id.layout_info);
        }

        public void setName(String name){
            tvName.setText(name);
        }

        public void setImage(String urlImage) {
            // Get image from URL
            Picasso.with(context)
                    .load(urlImage)
                    .placeholder(R.drawable.avatar_default)
                    .transform(new CircleTransform())
                    .into(ivPhoto);
        }

        public void setEventClick(final String pid) {
            // Set event click for all item to open the profile
            ProfileClickListener chatClickListener = new ProfileClickListener(pid);
            layout_info.setOnClickListener(chatClickListener);
        }

        class ProfileClickListener implements View.OnClickListener {
            private final String pid;

            public ProfileClickListener(String pid) {
                this.pid = pid;
            }

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("pid", pid);
                i.putExtras(b);
                view.getContext().startActivity(i);
            }
        }

        public void setDescription(String description) {
            tvDescription.setText(description);
        }

        public void hideDate() {
            tvDate.setVisibility(View.GONE);
        }

    }

}
