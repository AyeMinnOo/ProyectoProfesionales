package com.homesolution.app.ui.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.homesolution.app.Global;
import com.homesolution.app.io.response.CategoriasResponse;
import com.homesolution.app.ui.fragment.AgendaFragment;
import com.homesolution.app.ui.fragment.ChatsFragment;
import com.homesolution.app.ui.settings.SettingsActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.Category;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.ui.fragment.BusquedaFragment;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/*
    This is the main activity after the authentication.
    It contains three tabs and a view pager.
 */
public class PanelActivity extends AppCompatActivity {

    // Global variables
    private Global global;

    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;

    // Just one load of categories
    public static ArrayList<Category> categoryList;
    public static ProgressDialog progressDialog;

    @TargetApi(16)
    @Override
    public void onBackPressed() {
        final int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= 16) { // API 16
            finishAffinity();
        } else {
            finish();
        }
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

        // Global variables instance
        global = getGlobal();
    }

    private Global getGlobal() {
        if (global == null)
            global = (Global) getApplicationContext();

        return global;
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
                Log.e("Test/Panel", t.getLocalizedMessage());
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

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> tabTitles) {
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
