package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.RegistroResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.ZonasResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private Button btnRealizarRegistro;
    private Button btnIrLogin;

    private EditText etNombre;
    private Spinner spinnerZona;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    public void onBackPressed() {
        // To disable the back button
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Getting info from the previous activity
        // Bundle extras = getIntent().getExtras();
        // String zonas[] = extras.getStringArray("zonas");

        // Get request to load the list
        cargarZonas();

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

    private void cargarZonas() {
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
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setMessage("Cargando datos ...");
        progressDialog.show();
    }

    private void spinnerOptions(ArrayList<String> zonas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, zonas);
        spinnerZona.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarRegistro:
                realizarRegistro();
                Toast.makeText(this, "Registro realizado con éxito !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnIrLogin:
                goToLoginActivity();
                break;
        }
    }

    private void realizarRegistro() {
        // User data
        String nombre = etNombre.getText().toString();
        String zona = spinnerZona.getSelectedItem().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Call<RegistroResponse> call = HomeSolutionApiAdapter.getApiService().getRegistroResponse(nombre, email, password, 1, zona);
        call.enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Response<RegistroResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 0) {
                        Toast.makeText(getBaseContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Registro satisfactorio !", Toast.LENGTH_SHORT).show();
                        goToLoginActivity();
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setMessage("Procesando registro ...");
        progressDialog.show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
