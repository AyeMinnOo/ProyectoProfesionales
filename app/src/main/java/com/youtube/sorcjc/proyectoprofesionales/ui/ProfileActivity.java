package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Rating;
import com.youtube.sorcjc.proyectoprofesionales.domain.Skill;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerBasic;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerData;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerProfile;
import com.youtube.sorcjc.proyectoprofesionales.io.AgendarResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.PrestadorResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProfileActivity extends AppCompatActivity implements Callback<PrestadorResponse>, View.OnClickListener {

    // Worker data
    private String pid;

    // User authenticated data
    private static String token;

    // Profile views
    private ImageView ivBigPhoto;

    // Custom toolbar and actionbar
    private Toolbar toolbar;
    private TextView tvTitulo;
    private TextView tvSubtitulo;

    // Worker display data
    private TextView tvContenido1;
    private TextView tvContenido2;
    private TextView tvContenido3;
    private TextView tvContenido4;

    // Worker parameter data (start chat)
    private String uid;
    private String catstr;
    private String name;

    // Actions
    private Button btnCalificar;
    private Button btnAgendar;
    private Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpActionBar();

        if (pid == null) {
            Bundle b = getIntent().getExtras();
            pid = b.getString("pid");

            Log.d("Test/Profile", "Loading profile with pid => " + pid);
            loadAuthenticatedUser();
            loadProfile();
        }

        btnCalificar = (Button) findViewById(R.id.btnCalificar);
        btnCalificar.setOnClickListener(this);
        btnAgendar = (Button) findViewById(R.id.btnAgendar);
        btnAgendar.setOnClickListener(this);
        btnChat = (Button) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        ivBigPhoto = (ImageView) findViewById(R.id.ivBigPhoto);
        tvContenido1 = (TextView) findViewById(R.id.card_basic).findViewById(R.id.tvContenido);
        tvContenido2 = (TextView) findViewById(R.id.card_skills).findViewById(R.id.tvContenido);
        tvContenido3 = (TextView) findViewById(R.id.card_ratings).findViewById(R.id.tvContenido);
        tvContenido4 = (TextView) findViewById(R.id.card_certifications).findViewById(R.id.tvContenido);
        setCardTitles();
    }

    private void setCardTitles() {
        final TextView tvTitulo1 = (TextView) findViewById(R.id.card_basic).findViewById(R.id.tvTitulo);
        final TextView tvTitulo2 = (TextView) findViewById(R.id.card_skills).findViewById(R.id.tvTitulo);
        final TextView tvTitulo3 = (TextView) findViewById(R.id.card_ratings).findViewById(R.id.tvTitulo);
        final TextView tvTitulo4 = (TextView) findViewById(R.id.card_certifications).findViewById(R.id.tvTitulo);

        tvTitulo1.setText("Información");
        tvTitulo2.setText("Habilidades");
        tvTitulo3.setText("Calificaciones");
        tvTitulo4.setText("Certificaciones");
    }

    private void setUpActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        tvSubtitulo = (TextView) findViewById(R.id.tvSubtitulo);
    }

    private void loadAuthenticatedUser() {
        if (token == null) {
            final Global global = (Global) getApplicationContext();
            token = global.getToken();
        }
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

    private void loadProfile() {
        Call<PrestadorResponse> call = HomeSolutionApiAdapter.getApiService().getPrestador(token, pid);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<PrestadorResponse> response, Retrofit retrofit) {
        if (response.body() == null)
            return;

        if (response.body().getStatus() == 0) {
            Toast.makeText(this, response.body().getError(), Toast.LENGTH_SHORT).show();
        } else {
            WorkerProfile workerProfile = response.body().getResponse();
            // Setting the profile image
            RequestCreator request = Picasso.with(getBaseContext())
                    .load(workerProfile.getPicture())
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait);
            //request.into(ivPhoto);
            request.into(ivBigPhoto);

            WorkerData workerData = workerProfile.getPrestador();
            WorkerBasic workerBasic = workerData.getBasico();

            tvTitulo.setText(workerBasic.getName());
            tvSubtitulo.setText(workerData.getEstrellitas() + " estrellas");

            tvContenido1.setText(workerBasic.toString());

            String contenido2 = "";
            ArrayList<Skill> skills = workerData.getSkills();
            for (Skill skill : skills) {
                contenido2 += "- "+skill.getName()+"\n";
            }
            tvContenido2.setText(contenido2);

            String contenido3 = "";
            ArrayList<Rating> ratings = workerData.getRatings();
            for (Rating rating : ratings) {
                contenido3 += "- "+rating.getComment()+"\n";
            }
            if (contenido3.isEmpty())
                contenido3 = "Este usuario no ha recibido ninguna calificación.";
            tvContenido3.setText(contenido3);

            tvContenido4.setText(workerBasic.getCertifications().toString());

            uid = workerBasic.getUid();
            catstr = workerBasic.getCatstr();
            name = workerBasic.getName();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCalificar:
                break;

            case R.id.btnAgendar:
                Call<AgendarResponse> call = HomeSolutionApiAdapter.getApiService().getAgendar(token, pid);
                call.enqueue(new Callback<AgendarResponse>() {
                    @Override
                    public void onResponse(Response<AgendarResponse> response, Retrofit retrofit) {
                        if (response.body() == null)
                            return;

                        if (response.body().getStatus() == 0) {
                            Toast.makeText(ProfileActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.body().getResponse())
                                Toast.makeText(ProfileActivity.this, "Contacto agregado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
                break;

            case R.id.btnChat:
                Intent i = new Intent(view.getContext(), TalkActivity.class);
                Bundle b = new Bundle();
                b.putString("uid", uid);
                b.putString("catstr", catstr);
                b.putString("name", name);
                i.putExtras(b);
                startActivity(i);
                break;
        }
    }
}
