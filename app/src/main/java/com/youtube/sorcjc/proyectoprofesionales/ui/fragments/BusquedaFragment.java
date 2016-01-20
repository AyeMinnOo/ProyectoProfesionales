package com.youtube.sorcjc.proyectoprofesionales.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;
import com.youtube.sorcjc.proyectoprofesionales.ui.TalkActivity;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.MessageAdapter;

import java.util.ArrayList;

public class BusquedaFragment extends Fragment implements View.OnClickListener {

    //        TEST
    private Button btnTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_busqueda, container, false);

        btnTest = (Button) rootView.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), TalkActivity.class);
        startActivity(i);
    }

}
