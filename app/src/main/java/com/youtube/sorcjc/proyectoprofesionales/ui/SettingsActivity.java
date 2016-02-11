package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    // Available options
    private TextView tvUpdateUserData;
    private TextView tvTermsAndConditions;
    private TextView tvLogout;

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
                break;

            case R.id.tvTermsAndConditions:
                Intent i = new Intent(this, TermsConditionsActivity.class);
                startActivity(i);
                break;

            case R.id.tvLogout:
                break;
        }
    }
}
