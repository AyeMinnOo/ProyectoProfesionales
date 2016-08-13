package com.homesolution.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.Worker;
import com.homesolution.app.io.response.AgendaResponse;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.ui.adapter.WorkerAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AgendaFragment extends Fragment implements Callback<AgendaResponse> {

    // RecyclerView to list the contacts
    private RecyclerView recyclerView;

    // Used to render the contacts
    private static AgendaFragment agendaFragment;
    private static Activity activity;
    private static WorkerAdapter adapter;

    // Authenticated user data
    private static String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        agendaFragment = this;
        activity = getActivity();
        adapter = new WorkerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        // Get references to the views and controls
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewAgenda);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Loading contacts
        loadContacts();
    }

    public static void loadContacts() {
        final Global global = (Global) activity.getApplicationContext();
        token = global.getToken();

        Call<AgendaResponse> call = HomeSolutionApiAdapter.getApiService().getAgendaResponse(token);
        call.enqueue(agendaFragment);
    }

    @Override
    public void onResponse(Response<AgendaResponse> response, Retrofit retrofit) {
        ArrayList<Worker> workers = response.body().getResponse();
        adapter.setAll(workers);

        final Global global = (Global) activity.getApplicationContext();
        global.setContacts(workers);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
