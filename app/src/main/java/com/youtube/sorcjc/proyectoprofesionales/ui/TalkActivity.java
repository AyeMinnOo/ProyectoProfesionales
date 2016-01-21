package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.MessageAdapter;

import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener {

    /*private TabHost host;*/

    private Button btnPerfil;
    private Button btnCalificar;
    private Button btnLlamar;

    // Views in fragment_busqueda.xml
    private RecyclerView recyclerView;

    // Used to render the messages
    private static MessageAdapter adapter;

    // To send a message
    private ImageButton btnSend;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        adapter = new MessageAdapter(this);

        // Setup the tabs
        setUpTabs();

        // Get references to the views and controls
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // call smooth scroll
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
        recyclerView.setAdapter(adapter);

        etMessage = (EditText) findViewById(R.id.etMessage);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        // Loading messages
        loadMessages();
    }

    private void setUpTabs() {
        /*host = (TabHost)findViewById(R.id.tab_host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab_one_container);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab_two_container);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab_three_container);
        spec.setIndicator("Tab Three");
        host.addTab(spec);

        host.setOnTabChangedListener(this);*/

        btnPerfil = (Button) findViewById(R.id.btnPerfil);
        btnCalificar = (Button) findViewById(R.id.btnCalificar);
        btnLlamar = (Button) findViewById(R.id.btnLlamar);

        btnPerfil.setOnClickListener(this);
        btnCalificar.setOnClickListener(this);
        btnLlamar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPerfil:
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCalificar:
                Toast.makeText(this, "Calificar", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnLlamar:
                Toast.makeText(this, "Llamar", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSend:
                postMessage();
                break;
        }
    }

    private void postMessage() {
        String message = etMessage.getText().toString();
        adapter.addItem(new Message(message, "8.00 PM", true));
        etMessage.setText("");
    }

    private void loadMessages() {
        ArrayList<Message> examples = new ArrayList<>();
        examples.add(new Message("Hola, cómo estás?", "4.30 PM", false));
        examples.add(new Message("Estás ahí?", "4.45 PM", false));
        examples.add(new Message("Sí, dime ...", "4.50 PM", true));
        examples.add(new Message("Quería hacerte una consulta", "4.55 PM", false));
        adapter.addAll(examples);
    }

/*
    @Override
    public void onTabChanged(String s) {
        int i = host.getCurrentTab();
        Log.i("Test/TabHost", "Tab clicked =>" + i);
    }*/
}
