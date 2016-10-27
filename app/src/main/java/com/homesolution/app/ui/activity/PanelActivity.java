package com.homesolution.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.homesolution.app.Global;
import com.homesolution.app.domain.Category;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.CategoriasResponse;
import com.homesolution.app.io.response.SimpleResponse;
import com.homesolution.app.io.service.TrackingService;
import com.homesolution.app.ui.LoginActivity;
import com.homesolution.app.ui.fragment.AgendaFragment;
import com.homesolution.app.ui.fragment.BusquedaFragment;
import com.homesolution.app.ui.fragment.ChatsFragment;
import com.homesolution.app.ui.settings.SettingsActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;

import java.util.ArrayList;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/*
    This panel activity is shown after the authentication.
    It contains three tabs and a view pager.
 */
public class PanelActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Global variables
    private Global global;

    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;

    // Just one load of categories
    public static ArrayList<Category> categoryList;
    public static ProgressDialog progressDialog;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        // Data required by the fragments
        loadCategories();

        // Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Setting the toolbar
        if (toolbar != null)
            setSupportActionBar(toolbar);

        // Global instance
        global = getGlobal();

        // If the current user is worker
        if (global.isGeoActive()) // and have the geo mode ON
            startTrackingService();

        // Create an instance of GoogleAPIClient
        // just for non-workers
        if (! global.isPrestador() && mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getApplicationContext();

        return global;
    }

    private void startTrackingService() {
        startService(new Intent(this, TrackingService.class));
    }

    protected void onStart() {
        super.onStart();

        // Avoid google api client for workers
        if (getGlobal().isPrestador()) return;

        // Use the google api client for no-workers
        if (thirtyMinutesHasPassed())
            mGoogleApiClient.connect();
    }

    private boolean thirtyMinutesHasPassed() {
        float lastUpdate = getGlobal().getLastGeoUpdate();
        if (new Date().getTime() - lastUpdate > 30 * 60 * 1000)
            return true;
        return false;
    }

    protected void onStop() {
        // Only stop if it's connected (to avoid crash)
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = null;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } catch (SecurityException ex) {
            Toast.makeText(this, "Por favor habilite los permisos de GPS", Toast.LENGTH_SHORT).show();
        }

        if (mLastLocation == null)
            return;

        // Location retrieved is not null
        final String latitude = String.valueOf(mLastLocation.getLatitude());
        final String longitude = String.valueOf(mLastLocation.getLongitude());

        // Perform network I/O
        Global global = (Global) getApplicationContext();
        final String token = global.getToken();
        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService(global.getCountry())
                .getLatLng(token, latitude, longitude);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                SimpleResponse bodyResponse = response.body();
                if (bodyResponse != null) {
                    final boolean okResponse = bodyResponse.getResponse();
                    if (okResponse) {
                        updateGeoSharedPreferences();
                        // Log.d("GeneralGeo", "Coordenadas reportadas");
                    }
                } else {
                    Toast.makeText(PanelActivity.this, R.string.invalid_token, Toast.LENGTH_SHORT).show();
                    logoutWhenInvalidToken();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PanelActivity.this, R.string.retrofit_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutWhenInvalidToken() {
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
    }

    private void updateGeoSharedPreferences() {
        // Update the SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getString(R.string.geo_data), new Date().getTime());
        editor.apply();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Service disconnected or network lost
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void loadCategories() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();
        // Log.d("Test/Panel", "Categories are loading ...");

        Call<CategoriasResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                        .getCategoriasResponse();
        call.enqueue(new Callback<CategoriasResponse>() {
            @Override
            public void onResponse(Response<CategoriasResponse> response, Retrofit retrofit) {
                categoryList = response.body().getResponse();
                // Log.d("Test/Panel", "Categories are ready => " + categoryList.size());

                // After load categories ...
                setupViewPager();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log.e("Test/Panel", t.getLocalizedMessage());
            }
        });

    }

    private void setupViewPager() {
        // Setting the view pager
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), buildFragments(), buildTabTitles());
        viewPager.setAdapter(pagerAdapter);

        // Some delay to prevent empty titles
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Fragment> buildFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new ChatsFragment());
        fragments.add(new AgendaFragment());
        fragments.add(new BusquedaFragment());

        return fragments;
    }

    private ArrayList<String> buildTabTitles() {
        ArrayList<String> tabTitles = new ArrayList<>();

        tabTitles.add("Chat");
        tabTitles.add("Agenda");
        tabTitles.add("Búsqueda");

        return tabTitles;
    }

    public class PagerAdapter extends FragmentPagerAdapter{

        private ArrayList<String> tabTitles;
        private ArrayList<Fragment> fragments;

        PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> tabTitles) {
            super(fm);
            this.fragments = fragments;
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

    }
}
