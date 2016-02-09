package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.CategoriasResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.AgendaFragment;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.BusquedaFragment;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.ChatFragment;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PanelActivity extends AppCompatActivity {

    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;

    // Just one load of categories
    public static ArrayList<Category> categoryList;
    public static ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        confirmExit().show();
    }

    public AlertDialog confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar")
                .setMessage("¿Está seguro que desea cerrar sesión?")
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setNegativeButton("No", null);

        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        // Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Setting the view pager
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), buildFragments(), buildTabTitles());
        viewPager.setAdapter(pagerAdapter);

        // Some delay to prevent empty titles
        tabLayout.setupWithViewPager(viewPager);

        // Setting the toolbar
        if (toolbar != null)
            setSupportActionBar(toolbar);
    }

    private void loadCategories() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos ...");
        progressDialog.show();

        Log.d("Test/Panel", "Categories are loading ...");

        Call<CategoriasResponse> call = HomeSolutionApiAdapter.getApiService().getCategoriasResponse();
        call.enqueue(new Callback<CategoriasResponse>() {
            @Override
            public void onResponse(Response<CategoriasResponse> response, Retrofit retrofit) {
                ArrayList<Category> categories = response.body().getResponse();
                categoryList = categories;
                Log.d("Test/Panel", "Categories are ready => " + categories.size());
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PanelActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Fragment> buildFragments() {
        // Data required by the fragments
        loadCategories();

        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new ChatFragment());
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
