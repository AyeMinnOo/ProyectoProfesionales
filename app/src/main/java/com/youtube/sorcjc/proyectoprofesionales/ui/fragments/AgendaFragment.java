package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Worker;
import com.youtube.sorcjc.proyectoprofesionales.io.AgendaResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.WorkerAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AgendaFragment extends Fragment implements Callback<AgendaResponse> {

    // Views in fragment_agenda.xml
    private RecyclerView recyclerView;

    // Used to render the messages
    private static WorkerAdapter adapter;

    // Authenticated user data
    private static String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private void loadContacts() {
        if (token == null) {
            final Global global = (Global) getActivity().getApplicationContext();
            token = global.getToken();
        }

        Call<AgendaResponse> call = HomeSolutionApiAdapter.getApiService().getAgendaResponse(token);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<AgendaResponse> response, Retrofit retrofit) {
        ArrayList<Worker> workers = response.body().getResponse();
        adapter.addAll(workers);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
