package com.homesolution.app.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.homesolution.app.io.service.TrackingService;
import com.homesolution.app.ui.LoginActivity;
import com.homesolution.app.Global;
import com.homesolution.app.ui.activity.TalkActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.SimpleResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, Callback<SimpleResponse> {

    private static final String TAG = "DebugSettings";

    // Available options
    private TextView tvUpdateUserData;
    private Switch switchGeoMode;
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

        // Geolocation (only for workers)
        if (getGlobal().isPrestador()) {
            switchGeoMode = (Switch) findViewById(R.id.switchGeoMode);
            switchGeoMode.setOnClickListener(this);
            switchGeoMode.setChecked(getGlobal().isGeoActive());
        } else {
            View layoutGeolocation = findViewById(R.id.layout_geo);
            layoutGeolocation.setVisibility(View.GONE);
        }
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getApplicationContext();

        return global;
    }

    private void setUpActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(R.string.settings_title);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUpdateUserData:
                Intent iUpdateUser = new Intent(this, UpdateUserActivity.class);
                startActivity(iUpdateUser);
                break;

            case R.id.switchGeoMode:
                if (switchGeoMode.isChecked()) {
                    startService(new Intent(this, TrackingService.class));
                    Toast.makeText(SettingsActivity.this, R.string.settings_geo_activating, Toast.LENGTH_SHORT).show();
                } else {
                    requestGeolocationOff();
                }
                getGlobal().setGeoMode(switchGeoMode.isChecked());
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

    private void requestGeolocationOff() {
        final String token = getGlobal().getToken();

        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                .getGeoOff(token);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    stopService(new Intent(SettingsActivity.this, TrackingService.class));
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestLogout() {
        final String token = getGlobal().getToken();

        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                                            .getLogoutResponse(token);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
        if (response.body() != null && response.body().getStatus() == 1) {
            // Clear session from global variable
            getGlobal().setUserAuthenticated(null);

            // Clear session from SharedPreferences
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.user_data), "");
            editor.apply();

            // Clear static variables
            TalkActivity.token = null;

            // Stop tracking service
            stopService(new Intent(this, TrackingService.class));

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
