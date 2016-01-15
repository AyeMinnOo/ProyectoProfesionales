package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.io.CategoriasResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.ChatResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.CategoryAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.MessageAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatFragment extends Fragment implements Callback<CategoriasResponse> {

    // Views and controls in fragment_chat.xml
    private TextView tvNoMessages;
    private FrameLayout layoutBuscar;
    private RecyclerView recyclerView;
    private LinearLayout layoutIrBusqueda;

    // Used to render the messages
    private static MessageAdapter messageAdapter;

    // Used to render the categories
    private static CategoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CategoryAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Get references to the views and controls
        tvNoMessages = (TextView) rootView.findViewById(R.id.tvNoMessages);
        layoutBuscar = (FrameLayout) rootView.findViewById(R.id.layoutBuscar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewChat);
        layoutIrBusqueda = (LinearLayout) rootView.findViewById(R.id.layoutIrBusqueda);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // What adapter we will use?

        // Try to load active chats
        // Use the ChatAdapter if there are messages
        // But use CategoriesAdapter if there aren't messages

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the categories (and images)
        loadCategories();
    }

    private void loadMessages() {
        String testToken = "813abd218962ff966b54d26915388ecf"; // next, have to be modified
        Call<ChatResponse> call = HomeSolutionApiAdapter.getApiService().getChatResponse(testToken);
        // call.enqueue(this);
        // this class can't implement double callback<T>
        // we can use 2 anonymous functions
        // right now, i will look for a better way
    }

    private void loadCategories() {
        Call<CategoriasResponse> call = HomeSolutionApiAdapter.getApiService().getCategoriasResponse();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<CategoriasResponse> response, Retrofit retrofit) {
        ArrayList<Category> categories = response.body().getResponse();
        adapter.addAll(categories);
        // Toast.makeText(getActivity(), "Cargando resultados ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}