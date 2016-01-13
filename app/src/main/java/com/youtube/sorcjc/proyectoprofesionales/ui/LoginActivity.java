package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRealizarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRealizarLogin = (Button) findViewById(R.id.btnRealizarLogin);
        btnRealizarLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.btnRealizarLogin:
                intent = new Intent(this, PanelActivity.class);
                break;

        }

        if (intent != null)
            startActivity(intent);
    }

}
