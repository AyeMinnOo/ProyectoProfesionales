package com.youtube.sorcjc.proyectoprofesionales.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CategoriaViewHolder> {

    private ArrayList<Message> categories;
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
        this.categories = new ArrayList<>();
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        Message category = categories.get(position);

        holder.setName(category.getName());
        holder.setImage(category.getUrlImage());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addAll(@NonNull ArrayList<Message> categories) {
        if (categories == null)
            throw new NullPointerException("The results cannot be null.");

        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCategoria;
        private TextView tvName;

        public CategoriaViewHolder(View itemView) {
            super(itemView);

            ivCategoria = (ImageView) itemView.findViewById(R.id.ivCategoria);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }

        public void setName(String name){
            tvName.setText(name);
        }

        public void setImage(String urlImage) {
            Picasso.with(context)
                    .load(urlImage)
                    .placeholder(R.drawable.ic_category_default)
                    .into(ivCategoria);
        }

    }

}
