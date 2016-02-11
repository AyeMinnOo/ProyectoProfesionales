package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class TermsConditionsActivity extends AppCompatActivity {

    private TextView tvTermsAndConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        // Getting an action bar instance
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Términos y condiciones");
        }

        setTermsAndConditions();
    }

    private void setTermsAndConditions() {
        tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setText(R.string.terms_conditions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
