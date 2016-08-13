package com.homesolution.app.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.domain.Certifications;
import com.homesolution.app.domain.Rating;
import com.homesolution.app.domain.Skill;
import com.homesolution.app.domain.WorkerData;
import com.homesolution.app.ui.ScoreActivity;
import com.homesolution.app.ui.fragment.AgendaFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.Category;
import com.homesolution.app.domain.WorkerBasic;
import com.homesolution.app.domain.WorkerProfile;
import com.homesolution.app.io.response.SimpleResponse;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.PrestadorResponse;

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
    private RatingBar rbEstrellas;

    // Worker display data
    private CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardViewGallery;
    private TextView description1, description2, description3, description4, description5;

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
        getCards();
        getCardDescriptions();
        getCardTitles();
    }

    private void getCards() {
        cardView1 = (CardView) findViewById(R.id.card_basic);
        cardView2 = (CardView) findViewById(R.id.card_areas);
        cardView3 = (CardView) findViewById(R.id.card_skills);
        cardView4 = (CardView) findViewById(R.id.card_ratings);
        cardView5 = (CardView) findViewById(R.id.card_certifications);
        cardViewGallery = (CardView) findViewById(R.id.card_gallery);
    }

    private void getCardDescriptions() {
        description1 = (TextView) cardView1.findViewById(R.id.tvContenido);
        description2 = (TextView) cardView2.findViewById(R.id.tvContenido);
        description3 = (TextView) cardView3.findViewById(R.id.tvContenido);
        description4 = (TextView) cardView4.findViewById(R.id.tvContenido);
        description5 = (TextView) cardView5.findViewById(R.id.tvContenido);
    }

    private void getCardTitles() {
        final TextView tvTitulo1 = (TextView) cardView1.findViewById(R.id.tvTitulo);
        final TextView tvTitulo2 = (TextView) cardView2.findViewById(R.id.tvTitulo);
        final TextView tvTitulo3 = (TextView) cardView3.findViewById(R.id.tvTitulo);
        final TextView tvTitulo4 = (TextView) cardView4.findViewById(R.id.tvTitulo);
        final TextView tvTitulo5 = (TextView) cardView5.findViewById(R.id.tvTitulo);

        tvTitulo1.setText("Descripción");
        tvTitulo2.setText("Áreas");
        tvTitulo3.setText("Habilidades");
        tvTitulo4.setText("Certificaciones");
        tvTitulo5.setText("Calificaciones");
    }

    private void setUpActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        rbEstrellas = (RatingBar) findViewById(R.id.rbEstrellas);
    }

    private void loadAuthenticatedUser() {
        final Global global = (Global) getApplicationContext();
        token = global.getToken();
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
            btnAgendar.setText(getResources().getString(R.string.remove_contact));
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
                    .load(workerProfile.getPicture());
            //request.into(ivPhoto);
            request.into(ivBigPhoto);

            WorkerData workerData = workerProfile.getPrestador();
            WorkerBasic workerBasic = workerData.getBasico();

            categories = workerData.getCategories();
            saveCategoriesGlobal(categories);

            tvTitulo.setText(workerBasic.getName());
            rbEstrellas.setRating(workerData.getEstrellitas());

            // Basic information
            description1.setText( Html.fromHtml(workerBasic.toString()) );
            cardView1.setVisibility(View.VISIBLE);

            // Areas
            String content2 = workerBasic.getArea();
            if (! content2.isEmpty()) {
                description2.setText( content2 );
                cardView2.setVisibility(View.VISIBLE);
            }

            // Skills
            String content3 = "";
            ArrayList<Skill> skills = workerData.getSkills();
            for (Skill skill : skills) {
                content3 += "&#8226; "+skill.getName()+"<br />";
            }
            if (! content3.isEmpty()) {
                description3.setText( Html.fromHtml(content3) );
                cardView3.setVisibility(View.VISIBLE);
            }

            // Certifications
            final Certifications certifications = workerBasic.getCertifications();
            String content4 = "";
            if (certifications != null)
                content4 = certifications.toString();
            if (! content4.isEmpty()) {
                description4.setText( Html.fromHtml(content4) );
                cardView4.setVisibility(View.VISIBLE);
            }

            // Ratings
            String content5 = "";
            ArrayList<Rating> ratings = workerData.getRatings();
            for (Rating rating : ratings) {
                if (rating.getComment().length() > 1) {
                    content5 += "&#8226; ("+rating.getStars()+") "+rating.getComment()+"<br /><br />";
                }
            }
            if (! content5.isEmpty()) {
                description5.setText( Html.fromHtml(content5) );
                cardView5.setVisibility(View.VISIBLE);
            }

            // Gallery
            ArrayList<String> gallery = workerProfile.getGallery();
            if (gallery.size() > 0) {
                loadImageGallery(R.id.ivA, gallery.get(0));
                if (gallery.size() > 1) {
                    loadImageGallery(R.id.ivB, gallery.get(1));
                    if (gallery.size() > 2)
                        loadImageGallery(R.id.ivC, gallery.get(2));
                }

                cardViewGallery.setVisibility(View.VISIBLE);
            }

            uid = workerBasic.getUid();
            catstr = workerBasic.getCatstr();
            name = workerBasic.getName();
        }
    }

    private void loadImageGallery(int idView, String urlImage) {
        ImageView imageView = (ImageView) cardViewGallery.findViewById(idView);
        Picasso.with(getApplicationContext())
                .load(urlImage)
                .placeholder(R.drawable.ic_category_default)
                .into(imageView);
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
                bScore.putString("name", name);

                iScore.putExtras(bScore);
                startActivity(iScore);
                break;

            case R.id.btnAgendar:
                final Call<SimpleResponse> call;
                final String successMessage;

                if (isContact) {
                    successMessage = getResources().getString(R.string.success_remove_contact);
                    call = HomeSolutionApiAdapter.getApiService().getDesagendar(token, pid);
                } else {
                    successMessage = getResources().getString(R.string.success_add_contact);
                    call = HomeSolutionApiAdapter.getApiService().getAgendar(token, pid);
                }

                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                        if (response.body() == null)
                            return;

                        if (response.body().getStatus() == 0) {
                            Toast.makeText(ProfileActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.body().getResponse()) {
                                Toast.makeText(ProfileActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                                AgendaFragment.loadContacts();

                                if (isContact) {
                                    btnAgendar.setText(getResources().getString(R.string.add_contact));
                                    isContact = false;
                                } else {
                                    btnAgendar.setText(getResources().getString(R.string.remove_contact));
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
