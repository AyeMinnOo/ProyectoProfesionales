package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.AgendaFragment;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.BusquedaFragment;
import com.youtube.sorcjc.proyectoprofesionales.ui.fragments.ChatFragment;

import java.util.ArrayList;

public class PanelActivity extends AppCompatActivity {

    private PagerAdapter pagerAdapter;

    private ViewPager mViewPager;

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        // Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Setting the view pager
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), buildFragments(), buildTabTitles());
        mViewPager.setAdapter(pagerAdapter);

        // Some delay to prevent empty titles
        tabLayout.setupWithViewPager(mViewPager);

        // Setting the toolbar
        if (toolbar != null)
            setSupportActionBar(toolbar);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Fragment> buildFragments() {
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
