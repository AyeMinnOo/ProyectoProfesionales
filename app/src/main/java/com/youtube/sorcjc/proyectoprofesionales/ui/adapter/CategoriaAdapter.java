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
import com.youtube.sorcjc.proyectoprofesionales.domain.Categoria;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private ArrayList<Categoria> categorias;
    private Context context;

    public CategoriaAdapter(Context context) {
        this.context = context;
        this.categorias = new ArrayList<>();
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);

        holder.setName(categoria.getName());
        holder.setImage(categoria.getUrlImage());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public void addAll(@NonNull ArrayList<Categoria> categorias) {
        if (categorias == null)
            throw new NullPointerException("The results cannot be null.");

        this.categorias.addAll(categorias);
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
