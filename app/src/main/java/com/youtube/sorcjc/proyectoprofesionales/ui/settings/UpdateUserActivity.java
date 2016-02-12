package com.youtube.sorcjc.proyectoprofesionales.ui.settings;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.youtube.sorcjc.proyectoprofesionales.R;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    // Controls
    private EditText etArea;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private ImageButton ibArea;
    private ImageButton ibEmail;
    private ImageButton ibUsername;
    private ImageButton ibPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // Getting an action bar instance
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Actualizar datos");
        }

        // Read-only
        etArea = (EditText) findViewById(R.id.etArea);
        etEmail.setEnabled(false);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setEnabled(false);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etUsername.setEnabled(false);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setEnabled(false);

        // Buttons
        ibArea = (ImageButton) findViewById(R.id.ibArea);
        ibArea.setOnClickListener(null);
        ibEmail = (ImageButton) findViewById(R.id.ibEmail);
        ibEmail.setOnClickListener(this);
        ibUsername = (ImageButton) findViewById(R.id.ibUsername);
        ibUsername.setOnClickListener(this);
        ibPassword = (ImageButton) findViewById(R.id.ibPassword);
        ibPassword.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibArea:
                break;

            case R.id.ibEmail:
                changeEmail();
                break;

            case R.id.ibUsername:
                break;

            case R.id.ibPassword:
                break;
        }
    }

    private void changeEmail() {
        showInputDialog("Nuevo e-mail:", etEmail.getText().toString());
    }

    private void showInputDialog(String question, String initialValue) {
        // Get prompts view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        // Set prompts.xml to alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        // Question and answer
        ((TextView) promptsView.findViewById(R.id.tvQuestion)).setText(question);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.etInput);
        userInput.setText(initialValue);

        // Set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // Get input and set it to editText
                                etEmail.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // Create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
