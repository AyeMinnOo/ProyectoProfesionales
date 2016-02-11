package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.domain.Rating;
import com.youtube.sorcjc.proyectoprofesionales.domain.Skill;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerBasic;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerData;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerProfile;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.AgendarResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.PrestadorResponse;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.AgendaFragment;

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

    // Worker parameter data (to start TalkActivity)
    private String uid;
    private String catstr;
    private String name;

    // Worker parameter data (to start ScoreActivity)
    private ArrayList<Category> categories;

    // Actions
    private Button btnCalificar;
    private Button btnAgendar;
    private Button btnChat;

    // Add or remove contact?
    private boolean isContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpActionBar();

        // Button references
        btnCalificar = (Button) findViewById(R.id.btnCalificar);
        btnCalificar.setOnClickListener(this);
        btnAgendar = (Button) findViewById(R.id.btnAgendar);
        btnAgendar.setOnClickListener(this);
        btnChat = (Button) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        if (pid == null) {
            Bundle b = getIntent().getExtras();
            pid = b.getString("pid");

            Log.d("Test/Profile", "Loading profile with pid => " + pid);
            loadAuthenticatedUser();
            loadProfile();
        }

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

        // If this user is a contact, change the text of button
        final Global global = (Global) getApplicationContext();
        isContact = global.isContact(pid);
        if (isContact)
            btnAgendar.setText("Desagendar");
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

            categories = workerData.getCategories();
            saveCategoriesGlobal(categories);

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

    private void saveCategoriesGlobal(ArrayList<Category> categories) {
        // Save the categories for the last selected worker
        final Global global = (Global) getApplicationContext();
        global.setCategories(categories);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCalificar:
                Intent iScore = new Intent(view.getContext(), ScoreActivity.class);
                Bundle bScore = new Bundle();
                bScore.putString("pid", pid);
                iScore.putExtras(bScore);
                startActivity(iScore);
                break;

            case R.id.btnAgendar:
                final Call<AgendarResponse> call;
                final String successMessage;

                if (isContact) {
                    successMessage = "Contacto eliminado exitosamente";
                    call = HomeSolutionApiAdapter.getApiService().getDesagendar(token, pid);
                } else {
                    successMessage = "Contacto agregado exitosamente";
                    call = HomeSolutionApiAdapter.getApiService().getAgendar(token, pid);
                }

                call.enqueue(new Callback<AgendarResponse>() {
                    @Override
                    public void onResponse(Response<AgendarResponse> response, Retrofit retrofit) {
                        if (response.body() == null)
                            return;

                        if (response.body().getStatus() == 0) {
                            Toast.makeText(ProfileActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.body().getResponse()) {
                                Toast.makeText(ProfileActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                                AgendaFragment.loadContacts();

                                if (isContact) {
                                    btnAgendar.setText("Agendar");
                                    isContact = false;
                                } else {
                                    btnAgendar.setText("Desagendar");
                                    isContact = true;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(ProfileActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btnChat:
                Intent iTalk = new Intent(view.getContext(), TalkActivity.class);
                Bundle bTalk = new Bundle();
                bTalk.putString("uid", uid);
                bTalk.putString("catstr", catstr);
                bTalk.putString("name", name);
                iTalk.putExtras(bTalk);
                startActivity(iTalk);
                break;
        }
    }
}
