package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Categoria;
import com.youtube.sorcjc.proyectoprofesionales.io.CategoriasResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.CategoriaAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatFragment extends Fragment implements Callback<CategoriasResponse> {

    private RecyclerView recyclerView;
    private static CategoriaAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CategoriaAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Setting the recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        /*final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);*/

        // Here we take the view controls

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the categories (and images)
        loadCategories();
    }

    private void loadCategories() {
        Call<CategoriasResponse> call = HomeSolutionApiAdapter.getApiService().getCategoriasResponse();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<CategoriasResponse> response, Retrofit retrofit) {
        ArrayList<Categoria> categorias = response.body().getResponse();
        adapter.addAll(categorias);
        // Toast.makeText(getActivity(), "Cargando resultados ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}