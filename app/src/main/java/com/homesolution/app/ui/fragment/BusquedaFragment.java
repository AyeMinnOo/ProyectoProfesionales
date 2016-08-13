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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.domain.Worker;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.AgendaResponse;
import com.homesolution.app.ui.activity.PanelActivity;
import com.homesolution.app.ui.adapter.CategoryAdapter;
import com.homesolution.app.ui.adapter.WorkerAdapter;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.Category;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BusquedaFragment extends Fragment implements View.OnClickListener, Callback<AgendaResponse> {

    // Global variables
    private Global global;
    private static String token;

    private RecyclerView recyclerView;
    private EditText etFilter;
    private ImageView ivBuscar;

    // Used to render the categories or workers
    private static CategoryAdapter categoryAdapter;
    private static WorkerAdapter workerAdapter;

    private ArrayList<Category> categoryList;

    // To manage requested search
    private static String requestedQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryAdapter = new CategoryAdapter(getActivity());
        workerAdapter = new WorkerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_busqueda, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(categoryAdapter);

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

        ivBuscar = (ImageView) rootView.findViewById(R.id.ivBuscar);
        ivBuscar.setOnClickListener(this);

        // Token
        token = getGlobal().getToken();

        // A search was requested?
        if (container.getTag() != null) {
            String queryText = container.getTag().toString();
            // Log.d("Test/Busqueda", "Tag value in the tabs => " + queryText);
            container.setTag(null);
            requestedQuery = queryText;
        }

        return rootView;
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getActivity().getApplicationContext();

        return global;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (categoryAdapter.getItemCount() == 0) {
            Log.d("Test/Busqueda", "Loading the full list of categories");
            categoryList = ((PanelActivity) getActivity()).categoryList;
            categoryAdapter.addAll(categoryList);
        }

        if (! requestedQuery.isEmpty()) {
            Log.d("Test/Busqueda", "requestedQuery => " + requestedQuery);
            searchWorkers(requestedQuery);
            recyclerView.setAdapter(workerAdapter);
            requestedQuery = "";
            // It was open since the search in chat fragment
            hideKeyboard();
        }
    }

    @Override
    public void onClick(View view) {
        // Filter categories
        String queryText = etFilter.getText().toString().trim();

        if (queryText.isEmpty()) {
            recyclerView.setAdapter(categoryAdapter);
        } else {
            searchWorkers(queryText);
            recyclerView.setAdapter(workerAdapter);
        }

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void searchWorkers(String query) {
        Call<AgendaResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                                            .getBuscar(query, token);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<AgendaResponse> response, Retrofit retrofit) {
        ArrayList<Worker> workers = response.body().getResponse();
        workerAdapter.setAll(workers);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
