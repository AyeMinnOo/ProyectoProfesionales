package com.youtube.sorcjc.proyectoprofesionales.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.RecuperarResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.SimpleResponse;
import com.youtube.sorcjc.proyectoprofesionales.ui.LoginActivity;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, Callback<SimpleResponse> {

    // Available options
    private TextView tvUpdateUserData;
    private TextView tvTermsAndConditions;
    private TextView tvLogout;

    // Global variables
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setUpActionBar();

        // View references
        tvUpdateUserData = (TextView) findViewById(R.id.tvUpdateUserData);
        tvUpdateUserData.setOnClickListener(this);
        tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setOnClickListener(this);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(this);

        // Global variables instance
        global = (Global) getApplicationContext();
    }

    private void setUpActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Opciones");
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
            case R.id.tvUpdateUserData:
                Intent iUpdateUser = new Intent(this, UpdateUserActivity.class);
                startActivity(iUpdateUser);
                break;

            case R.id.tvTermsAndConditions:
                Intent iTermsConditions = new Intent(this, TermsConditionsActivity.class);
                startActivity(iTermsConditions);
                break;

            case R.id.tvLogout:
                requestLogout();
                break;
        }
    }

    private void requestLogout() {
        // Global variables instance
        final String token = global.getToken();

        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getLogoutResponse(token);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
        if (response.body() != null && response.body().getStatus() == 1) {
            // Clear session from global variable
            global.setUserAuthenticated(null);

            // Clear session from SharedPreferences
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.user_data), "");
            editor.apply();

            // Back to LoginActivity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(SettingsActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(SettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
