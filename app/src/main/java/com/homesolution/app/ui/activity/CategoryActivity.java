package com.homesolution.app.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.domain.Worker;
import com.homesolution.app.io.response.AgendaResponse;
import com.homesolution.app.ui.adapter.WorkerAdapter;
import com.squareup.picasso.Picasso;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.io.HomeSolutionApiAdapter;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/*
    This activity display a list of professionals
    associated with the selected category.
*/
public class CategoryActivity extends AppCompatActivity {

    // Global variables
    private Global global;

    // Used to render the workers
    private RecyclerView recyclerView;
    private WorkerAdapter adapter;
    private ProgressDialog progressDialog;

    // Message showed for 0 results
    private CardView cardNoWorkers;

    // Custom action bar
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvDescription;

    // Static data
    private static String categoryId;
    private static String categoryName;
    private static String categoryPicture;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setUpActionBar();

        // Setting the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkerAdapter(this);
        recyclerView.setAdapter(adapter);

        // Bundle parameters from previous activity
        Bundle b = getIntent().getExtras();
        categoryId = b.getString("categoryId");
        categoryName = b.getString("categoryName");
        categoryPicture = b.getString("categoryPicture");

        // Message for 0 results
        cardNoWorkers = (CardView) findViewById(R.id.card_no_workers);

        // Global variables instance
        global = getGlobal();

        loadAuthenticatedUser();
        loadworkers();
    }

    private void setUpActionBar() {
        // Custom action bar
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_category, null);
        tvName = (TextView) mCustomView.findViewById(R.id.tvName);
        ivPhoto = (ImageView) mCustomView.findViewById(R.id.ivPhoto);
        tvDescription = (TextView) mCustomView.findViewById(R.id.tvDescription);

        mActionBar.setCustomView(mCustomView, new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Back button in action bar
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

    private void loadAuthenticatedUser() {
        if (token == null) {
            token = getGlobal().getToken();
        }
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getApplicationContext();

        return global;
    }

    private void loadworkers() {
        // Log.d("Test/WorkersList", "Loading workers for the categoryId => " + categoryId);
        Call<AgendaResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                    .getPrestadores(categoryId, token);

        call.enqueue(new Callback<AgendaResponse>() {
            @Override
            public void onResponse(Response<AgendaResponse> response, Retrofit retrofit) {
                if (response.body() == null) {
                    progressDialog.dismiss();
                    return;
                }

                if (response.body().getStatus() == 0) {
                    Toast.makeText(CategoryActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Worker> workers = response.body().getResponse();

                    // Number of results in action bar
                    tvName.setText(categoryName);
                    Picasso.with(getBaseContext())
                            .load(categoryPicture)
                            .into(ivPhoto);
                    tvDescription.setText(workers.size() + " resultados");

                    // Set workers
                    adapter.addAll(workers);

                    // No workers?
                    if (workers.isEmpty())
                        showNoWorkersMessage();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(CategoryActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Test/Category", t.getLocalizedMessage());
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando categoría ...");
        progressDialog.show();
    }

    private void showNoWorkersMessage() {
        cardNoWorkers.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
