package com.homesolution.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.domain.Chat;
import com.homesolution.app.ui.adapter.CategoryAdapter;
import com.homesolution.app.ui.adapter.ChatAdapter;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.io.response.ChatsResponse;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.ui.PanelActivity;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatsFragment extends Fragment implements View.OnClickListener {


    // Views and controls in fragment_chats.xmll
    private static LinearLayout layoutTop;
    private static RecyclerView recyclerView;
    private static LinearLayout layoutIrBusqueda;
    private ImageView ivIrBusqueda;

    // The search will be performed in the third tab
    private ImageView ivBuscar;
    private EditText etFilter;

    // Used to render the messages
    private static ChatAdapter chatAdapter;

    // Used to render the categories
    private static CategoryAdapter categoryAdapter;

    // User authenticated data
    private static String token;

    // Required to reload the active chats
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatAdapter = new ChatAdapter(getActivity());
        categoryAdapter = new CategoryAdapter(getActivity());

        context = getActivity();

        Log.d("Test/Chats", "onCreate fired !");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        // Get references to the views and controls
        layoutTop = (LinearLayout) rootView.findViewById(R.id.layoutTop);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewChat);
        layoutIrBusqueda = (LinearLayout) rootView.findViewById(R.id.layoutIrBusqueda);
        ivIrBusqueda = (ImageView) rootView.findViewById(R.id.ivIrBusqueda);

        ivBuscar = (ImageView) rootView.findViewById(R.id.ivBuscar);
        etFilter = (EditText) rootView.findViewById(R.id.etFilter);
        etFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Search when the user performs "done" in the keyboard
                    onClick(v);
                    return true;
                }
                return false;
            }
        });

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chatAdapter);

        // Click events
        ivIrBusqueda.setOnClickListener(this);
        ivBuscar.setOnClickListener(this);

        // Log.d("Test/Chats", "onCreateView fired !");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAuthenticatedUser();

        // Try to load active chats
        // Use the ChatAdapter if there are messages
        // But use CategoriesAdapter if there aren't messages
        loadActiveChats();

        // Log.d("Test/Chats", "onViewCreated fired !");
    }

    private void getAuthenticatedUser() {
        final Global global = (Global) getActivity().getApplicationContext();
        token = global.getToken();
    }

    public static void loadActiveChats() {
        Call<ChatsResponse> call = HomeSolutionApiAdapter.getApiService().getChatsResponse(token);

        call.enqueue(new Callback<ChatsResponse>() {
            @Override
            public void onResponse(Response<ChatsResponse> response, Retrofit retrofit) {
                ArrayList<Chat> chats = response.body().getResponse();

                if (chats.size() > 0) {
                    // Log.d("Test/Chat", "Active chats from WS => " + chats.size());
                    showMessages(chats);
                } else {
                    showCategories();
                }

                PanelActivity.progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ChatsFragment.context, R.string.retrofit_failure, Toast.LENGTH_SHORT).show();
                // Log.d("Test/Chat", "ChatsResponse onFailure => " + t.getLocalizedMessage());

                PanelActivity.progressDialog.dismiss();
            }
        });
    }

    public static void showMessages(ArrayList<Chat> chats) {
        // Hide top layout
        layoutTop.setVisibility(View.GONE);
        chatAdapter.setAll(chats);
    }

    public static void showCategories() {
        // Hide bottom layout
        layoutIrBusqueda.setVisibility(View.GONE);

        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.addAll(PanelActivity.categoryList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etFilter:
                // "Done" action from keyboard

            case R.id.ivBuscar:
                String queryText = etFilter.getText().toString().trim();

                // Empty value? So, nothing happens
                if (queryText.isEmpty())
                    break;

                // Defining a new tag value
                PanelActivity.viewPager.setTag(queryText);

                // Clear query
                etFilter.setText("");
                // break; isn't necessary

            case R.id.ivIrBusqueda:
                PanelActivity.viewPager.setCurrentItem(2, true);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Log.d("Test/Chats", "onResume fired !");
    }
}