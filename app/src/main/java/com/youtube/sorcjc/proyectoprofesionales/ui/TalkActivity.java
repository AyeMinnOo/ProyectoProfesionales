package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;
import com.youtube.sorcjc.proyectoprofesionales.io.ChatResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.Talk;
import com.youtube.sorcjc.proyectoprofesionales.ui.adapter.MessageAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPerfil;
    private Button btnCalificar;
    private Button btnLlamar;

    // Views in fragment_busqueda.xml
    private RecyclerView recyclerView;

    // Used to render the messages
    private MessageAdapter adapter;
    private ProgressDialog progressDialog;

    // To send a message
    private ImageButton btnSend;
    private EditText etMessage;

    // User authenticated data
    // Using static we just need one load
    private static String token;
    private static String uid;

    // User destination data
    private String toUid;
    private String catstr;
    // private String username;

    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        adapter = new MessageAdapter(this);

        // Setup the tabs
        setUpTabs();
        setUpActionBar();

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

        // Bundle parameters from previous activity
        if (toUid == null) {
            Bundle b = getIntent().getExtras();
            toUid = b.getString("uid");
            catstr = b.getString("catstr");
        }

        loadAuthenticatedUser();
        loadMessages();
    }

    private void loadAuthenticatedUser() {
        if (token == null) {
            final Global global = (Global) getApplicationContext();
            token = global.getToken();
            uid = global.getUid();
        }
    }

    private void setUpTabs() {
        btnPerfil = (Button) findViewById(R.id.btnPerfil);
        btnCalificar = (Button) findViewById(R.id.btnCalificar);
        btnLlamar = (Button) findViewById(R.id.btnLlamar);

        btnPerfil.setOnClickListener(this);
        btnCalificar.setOnClickListener(this);
        btnLlamar.setOnClickListener(this);
    }

    private void setUpActionBar() {
        // Custom action bar
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_talk, null);
        tvName = (TextView) mCustomView.findViewById(R.id.tvName);
        ivPhoto = (ImageView) mCustomView.findViewById(R.id.ivPhoto);
        tvDescription = (TextView) mCustomView.findViewById(R.id.tvDescription);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        final String message = etMessage.getText().toString();
/*
        Call<EnviarMsjeResponse> call = HomeSolutionApiAdapter.getApiService().getEnviarMensaje(token, uid, replyTo, message);
        call.enqueue(new Callback<EnviarMsjeResponse>() {
            @Override
            public void onResponse(Response<EnviarMsjeResponse> response, Retrofit retrofit) {
                if (response.body() == null)
                    return;

                if (response.body().getStatus() == 0) {
                    Toast.makeText(TalkActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Boolean sent = response.body().getResponse();
                    if (sent) {
                        adapter.addItem(new Message(message, "8.00 PM", true));
                        etMessage.setText("");
                    } else {
                        Toast.makeText(TalkActivity.this, "No se ha enviado el mensaje", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/
    }

    private void loadDummyMessages() {
        // Used for testing
        ArrayList<Message> examples = new ArrayList<>();
        examples.add(new Message("Hola, cómo estás?", "4.30 PM", false));
        examples.add(new Message("Estás ahí?", "4.45 PM", false));
        examples.add(new Message("Sí, dime ...", "4.50 PM", true));
        examples.add(new Message("Quería hacerte una consulta", "4.55 PM", false));
        adapter.addAll(examples);
    }

    private void loadMessages() {
        Log.d("Test/Talk", "Loading chat with the uid => " + toUid);
        Call<ChatResponse> call = HomeSolutionApiAdapter.getApiService().getChatResponse(token, toUid);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Response<ChatResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();

                if (response.body() == null)
                    return;

                if (response.body().getStatus() == 0) {
                    Toast.makeText(TalkActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Talk talk = response.body().getResponse();

                    // Set contact data
                    tvName.setText(talk.getUsername());
                    Picasso.with(getBaseContext())
                            .load(talk.getPicture())
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                            .into(ivPhoto);
                    tvDescription.setText(catstr);

                    // Set messages
                    adapter.addAll(talk.getChat());
                    Log.d("Test/Talk", "Number of messages in chat => " + talk.getChat().size());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TalkActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando mensajes ...");
        progressDialog.show();
    }

}
