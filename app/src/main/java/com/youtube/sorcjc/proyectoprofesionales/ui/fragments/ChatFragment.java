package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.domain.Chat;
import com.youtube.sorcjc.proyectoprofesionales.io.CategoriasResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.ChatResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.PanelActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.CategoryAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.ChatAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatFragment extends Fragment implements View.OnClickListener {


    // Views and controls in fragment_chat.xml
    private LinearLayout layoutTop;
    private RecyclerView recyclerView;
    private LinearLayout layoutIrBusqueda;
    private ImageView ivIrBusqueda;

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
        layoutTop = (LinearLayout) rootView.findViewById(R.id.layoutTop);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewChat);
        layoutIrBusqueda = (LinearLayout) rootView.findViewById(R.id.layoutIrBusqueda);
        ivIrBusqueda = (ImageView) rootView.findViewById(R.id.ivIrBusqueda);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chatAdapter);

        ivIrBusqueda.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Try to load active chats
        // Use the ChatAdapter if there are messages
        // But use CategoriesAdapter if there aren't messages
        loadMessages();
    }

    private void loadMessages() {
        String testToken = "813abd218962ff966b54d26915388ecf"; // next, have to be modified
        Call<ChatResponse> call = HomeSolutionApiAdapter.getApiService().getChatResponse(testToken);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Response<ChatResponse> response, Retrofit retrofit) {
                ArrayList<Chat> chats = response.body().getResponse();
                Log.d("Test/Chat", "# messages from WS => " + chats.size());

                if (chats.size() > 0) {
                    showMessages(chats);
                } else {
                    showCategories();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessages(ArrayList<Chat> chats) {
        // Hide top layout
        layoutTop.setVisibility(View.GONE);

//        if (recyclerView.getAdapter() == null)
//            recyclerView.setAdapter(chatAdapter);

        chatAdapter.addAll(chats);
    }

    private void showCategories() {
        // Hide bottom layout
        layoutIrBusqueda.setVisibility(View.GONE);

        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.addAll(PanelActivity.categoryList);
    }

    @Override
    public void onClick(View view) {
        PanelActivity.viewPager.setCurrentItem(2, true);
    }
}