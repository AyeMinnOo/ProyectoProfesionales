package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.LoginResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.ZonasResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse> {

    private Button btnRealizarLogin;

    private EditText etEmail;
    private EditText etPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting the view controls
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnRealizarLogin = (Button) findViewById(R.id.btnRealizarLogin);
        btnRealizarLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarLogin:
                validarLogin();
                break;
        }
    }

    private void validarLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        Log.d("Test/Login", "email => " + email);
        Log.d("Test/Login", "password => " + password);

        Call<LoginResponse> call = HomeSolutionApiAdapter.getApiService().getLoginResponse(email, password);
        call.enqueue(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando credenciales ...");
        progressDialog.show();
    }

    @Override
    public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
        progressDialog.dismiss();

        if (response.body() == null)
            return;

        if (response.body().getStatus() == 0) {
            Toast.makeText(this, response.body().getError(), Toast.LENGTH_SHORT).show();
        } else {
            UserAuthenticated userAuthenticated = response.body().getResponse();
            Log.i("Test/Login", "token => " + userAuthenticated.getToken());
            /* Intent intent = null;
            intent = new Intent(this, PanelActivity.class);
            if (intent != null)
                startActivity(intent);*/
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

}
