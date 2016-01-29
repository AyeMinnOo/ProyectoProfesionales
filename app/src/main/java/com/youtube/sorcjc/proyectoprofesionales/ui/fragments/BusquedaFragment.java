package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;
import com.youtube.sorcjc.proyectoprofesionales.ui.PanelActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.TalkActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.CategoryAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.MessageAdapter;

import java.util.ArrayList;

public class BusquedaFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText etFilter;
    private ImageView ivBuscar;

    // Used to render the categories
    private static CategoryAdapter adapter;
    private ArrayList<Category> categoryList;

    // To manage requested search
    private static String requestedQuery = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CategoryAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_busqueda, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        etFilter = (EditText) rootView.findViewById(R.id.etFilter);

        ivBuscar = (ImageView) rootView.findViewById(R.id.ivBuscar);
        ivBuscar.setOnClickListener(this);

        // A search was requested?
        if (container.getTag() != null) {
            String queryText = container.getTag().toString();
            Log.d("Test/Busqueda", "Reading the tag value => " + queryText);
            container.setTag(null);
            requestedQuery = queryText;
        }


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Test/Busqueda", "Loading the full list of categories");
        if (adapter.getItemCount() == 0) {
            categoryList = ((PanelActivity) getActivity()).categoryList;
            adapter.addAll(categoryList);
        }

        Log.d("Test/Busqueda", "requestedQuery => " + requestedQuery);
        if (! requestedQuery.isEmpty()) {
            adapter.setFilter(requestedQuery);
            Log.d("Test/Busqueda", "New filter for categories => " + requestedQuery);
            requestedQuery = "";
        }
    }

    @Override
    public void onClick(View view) {
        // Filter categories
        String queryText = etFilter.getText().toString().trim();

        Log.d("Test/Busqueda", "Applying filter to categories => " + queryText);
        if (queryText.isEmpty()) {
            adapter.flushFilter();
        } else {
            adapter.setFilter(queryText);
        }
    }


}
