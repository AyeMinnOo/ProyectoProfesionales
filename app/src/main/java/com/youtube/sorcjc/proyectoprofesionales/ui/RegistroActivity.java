package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.ZonasResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener, Callback<ZonasResponse> {

    private ProgressDialog progressDialog;

    private Spinner spinnerZona;
    private Button btnRealizarRegistro;

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
        btnRealizarRegistro = (Button) findViewById(R.id.btnRealizarRegistro);
        btnRealizarRegistro.setOnClickListener(this);
    }

    private void cargarZonas() {
        // We will use retrofit to get the list
        Call<ZonasResponse> call = HomeSolutionApiAdapter.getApiService().getZonasResponse();
        call.enqueue(this);
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setMessage("Cargando datos ...");
        progressDialog.show();
    }

    @Override
    public void onResponse(Response<ZonasResponse> response, Retrofit retrofit) {
        ArrayList<String> zonas = response.body().getResponse();

        // Setting the options in the spinner
        spinnerOptions(zonas);

        progressDialog.dismiss();
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void spinnerOptions(ArrayList<String> zonas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, zonas);
        spinnerZona.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarRegistro:
                Toast.makeText(this, "Registro realizado con éxito !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnIrLogin:
                // Start the LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

}
