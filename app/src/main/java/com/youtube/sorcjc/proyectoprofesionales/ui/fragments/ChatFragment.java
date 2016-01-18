package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.domain.Chat;
import com.youtube.sorcjc.proyectoprofesionales.io.CategoriasResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.ChatResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.CategoryAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.ChatAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatFragment extends Fragment {


    // Views and controls in fragment_chat.xml
    private LinearLayout layoutTop;
    private ScrollView scrollCentral;
    private RecyclerView recyclerView;
    private LinearLayout layoutIrBusqueda;

    // Used to render the messages
    private static ChatAdapter chatAdapter;

    // Used to render the categories
    private static CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatAdapter = new ChatAdapter(getActivity());
        categoryAdapter = new CategoryAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // Get references to the views and controls
        scrollCentral = (ScrollView) rootView.findViewById(R.id.scrollCentral);
        layoutTop = (LinearLayout) rootView.findViewById(R.id.layoutTop);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewChat);
        layoutIrBusqueda = (LinearLayout) rootView.findViewById(R.id.layoutIrBusqueda);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // What adapter we will use?

        // Try to load active chats
        loadMessages();
        // Use the ChatAdapter if there are messages
        // But use CategoriesAdapter if there aren't messages
    }

    private void hideTopLayout() {
        layoutTop.setVisibility(View.INVISIBLE);
        // Fixing the marginTop
        fixScrollMargins(0, convertToPx(92));
    }

    private void hideBottomLayout() {
        layoutIrBusqueda.setVisibility(View.INVISIBLE);
        // Fixing the marginBottom
        fixScrollMargins(convertToPx(109), 0);
    }

    private void fixScrollMargins(int top, int bottom) {
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, top, 0, bottom);
        scrollCentral.setLayoutParams(params);
    }

    private int convertToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private void loadMessages() {
        String testToken = "813abd218962ff966b54d26915388ecf"; // next, have to be modified
        Call<ChatResponse> call = HomeSolutionApiAdapter.getApiService().getChatResponse(testToken);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Response<ChatResponse> response, Retrofit retrofit) {
                ArrayList<Chat> chats = response.body().getResponse();
                if (chats.size() > 0) {
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.addAll(chats);
                    hideTopLayout();
                } else {
                    hideBottomLayout();
                    recyclerView.setAdapter(categoryAdapter);
                    loadCategories();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        Call<CategoriasResponse> call = HomeSolutionApiAdapter.getApiService().getCategoriasResponse();
        call.enqueue(new Callback<CategoriasResponse>() {
            @Override
            public void onResponse(Response<CategoriasResponse> response, Retrofit retrofit) {
                ArrayList<Category> categories = response.body().getResponse();
                categoryAdapter.addAll(categories);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}