package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class ProfileActivity extends AppCompatActivity {

    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (pid == null) {
            Bundle b = getIntent().getExtras();
            pid = b.getString("pid");
            Log.d("Test/Profile", "Profile for the pid selected => " + pid);
        }
    }
}
