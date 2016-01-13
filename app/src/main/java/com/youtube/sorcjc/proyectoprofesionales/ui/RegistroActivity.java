package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerZona;
    private Button btnRealizarRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Getting the list of zones
        Bundle extras = getIntent().getExtras();
        String zonas[] = extras.getStringArray("zonas");

        // View controls
        spinnerZona = (Spinner) findViewById(R.id.spinnerZona);
        btnRealizarRegistro = (Button) findViewById(R.id.btnRealizarRegistro);
        btnRealizarRegistro.setOnClickListener(this);

        // Setting the options in the spinner
        spinnerOptions(zonas);
    }

    private void spinnerOptions(String zonas[]) {
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
