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
import com.homesolution.app.domain.Category;
import com.homesolution.app.ui.CategoryActivity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoriaViewHolder> {

    private ArrayList<Category> allCategories;
    private ArrayList<Category> filteredCategories;
    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
        this.allCategories = new ArrayList<>();
        this.filteredCategories = new ArrayList<>();
    }

    // Undo the filter
    public void flushFilter(){
        filteredCategories = new ArrayList<>();
        filteredCategories.addAll(allCategories);
        notifyDataSetChanged();
    }

    // Apply a filter text
    public void setFilter(String queryText) {
        filteredCategories = new ArrayList<>();
        queryText = queryText.toLowerCase();

        for (Category category : allCategories) {
            if (category.getName().toLowerCase().contains(queryText))
                filteredCategories.add(category);
        }

        notifyDataSetChanged();
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        Category category = filteredCategories.get(position);

        holder.setName(category.getName());
        holder.setImage(category.getUrlImage());
        holder.setClickEvent(category.getCid(), category.getName(), category.getUrlImage());
    }

    @Override
    public int getItemCount() {
        return filteredCategories.size();
    }

    public void addAll(@NonNull ArrayList<Category> categories) {
        this.allCategories.addAll(categories);

        if (this.filteredCategories.size() == 0)
            this.filteredCategories.addAll(categories);

        notifyDataSetChanged();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder {
        // Category data
        private ImageView ivCategoria;
        private TextView tvName;
        // To cover the event click
        private LinearLayout layout_category;

        public CategoriaViewHolder(View itemView) {
            super(itemView);

            ivCategoria = (ImageView) itemView.findViewById(R.id.ivCategoria);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            layout_category = (LinearLayout) itemView.findViewById(R.id.layout_category);
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

        public void setClickEvent(final String categoryId, final String name, final String picture) {
            layout_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), CategoryActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("categoryId", categoryId);
                    mBundle.putString("categoryName", name);
                    mBundle.putString("categoryPicture", picture);
                    i.putExtras(mBundle);
                    context.startActivity(i);
                }
            });
        }

    }

}
