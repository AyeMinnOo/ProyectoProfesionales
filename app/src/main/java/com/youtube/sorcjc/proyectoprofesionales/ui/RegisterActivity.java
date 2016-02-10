package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.ZonasResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private Button btnRealizarRegistro;
    private Button btnIrLogin;

    private EditText etNombre;
    private Spinner spinnerZona;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    public void onBackPressed() {
        confirmExit().show();
    }

    public AlertDialog confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar")
                .setMessage("¿Está seguro que desea salir?")
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        })
                .setNegativeButton("No", null);

        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        // Get request to load the list
        loadAreasFromWS();

        // View controls
        spinnerZona = (Spinner) findViewById(R.id.spinnerZona);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        // Buttons
        btnRealizarRegistro = (Button) findViewById(R.id.btnRealizarRegistro);
        btnIrLogin = (Button) findViewById(R.id.btnIrLogin);

        btnRealizarRegistro.setOnClickListener(this);
        btnIrLogin.setOnClickListener(this);
    }

    private void loadAreasFromWS() {
        // We will use retrofit to get the list
        Call<ZonasResponse> call = HomeSolutionApiAdapter.getApiService().getZonasResponse();
        call.enqueue(new Callback<ZonasResponse>() {
            @Override
            public void onResponse(Response<ZonasResponse> response, Retrofit retrofit) {
                if (response.body() != null)
                {
                    ArrayList<String> zonas = response.body().getResponse();
                    // Setting the options in the spinner
                    spinnerOptions(zonas);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Cargando datos ...");
        progressDialog.show();
    }

    private void spinnerOptions(ArrayList<String> zonas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zonas);
        spinnerZona.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarRegistro:
                realizarRegistro();
                break;

            case R.id.btnIrLogin:
                goToLoginActivity();
                break;
        }
    }

    private String getGcmId() {
        final Global global = (Global) getApplicationContext();
        return global.getGcmId();
    }

    private void realizarRegistro() {
        // User data
        final String nombre = etNombre.getText().toString();
        final String zona = spinnerZona.getSelectedItem().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String gcm_id = getGcmId();

        if (nombre.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle data = new Bundle();
        data.putString("nombre", nombre);
        data.putString("zona", zona);
        data.putString("email", email);
        data.putString("password", password);
        data.putString("gcm_id", gcm_id);

        showTermsAndConditions(data);
    }

    private void showTermsAndConditions(Bundle parameters) {
        Intent i = new Intent(this, ConfirmRegisterActivity.class);
        i.putExtras(parameters);
        startActivity(i);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
