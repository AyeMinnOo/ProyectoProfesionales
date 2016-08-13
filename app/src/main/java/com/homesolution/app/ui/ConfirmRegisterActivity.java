package com.homesolution.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.homesolution.app.Global;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.LoginResponse;
import com.homesolution.app.ui.activity.PanelActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.UserAuthenticated;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ConfirmRegisterActivity extends AppCompatActivity {

    // Global variables
    private Global global;

    private ProgressDialog progressDialog;
    private TextView tvTermsAndConditions;

    // New user data
    private String nombre;
    private String zona;
    private String email;
    private String password;
    private String gcm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        // Getting an action bar instance
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTermsAndConditions();

        // Getting the parameters from register activity
        Bundle b = getIntent().getExtras();
        nombre = b.getString("nombre");
        zona = b.getString("zona");
        email = b.getString("email");
        password = b.getString("password");
        gcm_id = b.getString("gcm_id");
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getApplicationContext();

        return global;
    }

    private void setTermsAndConditions() {
        tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setText(R.string.terms_conditions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fullscreen_dialog_register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_save:
                callRegisterWS();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callRegisterWS() {
        Call<LoginResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                    .getRegistroResponse(nombre, email, password, 1, zona, gcm_id);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 0) {
                        Toast.makeText(getBaseContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getBaseContext(), "Registro satisfactorio !", Toast.LENGTH_SHORT).show();
                        UserAuthenticated userAuthenticated = response.body().getResponse();
                        autoLogin(userAuthenticated);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando registro ...");
        progressDialog.show();
    }

    private void autoLogin(UserAuthenticated userAuthenticated) {
        // Save the session in a global variable
        getGlobal().setUserAuthenticated(userAuthenticated);

        updateSharedPreferences(userAuthenticated);

        // Go to PanelActivity
        Intent intent = new Intent(this, PanelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateSharedPreferences(UserAuthenticated userAuthenticated) {
        // Using SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Start app with this activity
        editor.putString(getString(R.string.first_activity), ".ui.LoginActivity");

        // Save the user data in json format
        String userData = new Gson().toJson(userAuthenticated);
        editor.putString(getString(R.string.user_data), userData);

        editor.apply();
    }
}
