package com.youtube.sorcjc.proyectoprofesionales.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.SimpleResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.ZonasResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    // Controls
    private EditText etArea;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private ImageView ibArea;
    private ImageView ibEmail;
    private ImageView ibUsername;
    private ImageView ibPassword;

    // Prompts areas
    private Spinner spinnerAreas;
    private static final ArrayList<String> areas = new ArrayList<>();
    private static AlertDialog.Builder optionsDialogBuilder;

    // EditText field that will be modified
    private static final int EMAIL = 0;
    private static final int USERNAME = 1;
    private static final int PASSWORD = 2;

    // User authenticated data
    private Global global;
    private static String token;


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

        // Required to apply changes
        global =  (Global) getApplicationContext();
        token = global.getToken();

        // Read-only
        etArea = (EditText) findViewById(R.id.etArea);
        etArea.setEnabled(false);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setEnabled(false);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etUsername.setEnabled(false);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setEnabled(false);

        // Buttons
        ibArea = (ImageView) findViewById(R.id.ibArea);
        ibArea.setOnClickListener(this);
        ibEmail = (ImageView) findViewById(R.id.ibEmail);
        ibEmail.setOnClickListener(this);
        ibUsername = (ImageView) findViewById(R.id.ibUsername);
        ibUsername.setOnClickListener(this);
        ibPassword = (ImageView) findViewById(R.id.ibPassword);
        ibPassword.setOnClickListener(this);

        // Current data for the authenticated user
        etArea.setText(global.getArea());
        etEmail.setText(global.getEmail());
        etUsername.setText(global.getUsername());

        prepareAreaPrompts();
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
                changeArea();
                break;

            case R.id.ibEmail:
                changeEmail();
                break;

            case R.id.ibUsername:
                changeUsername();
                break;

            case R.id.ibPassword:
                changePassword();
                break;
        }
    }

    private void prepareAreaPrompts() {
        // Get prompts_input view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts_options, null);

        // Set prompts_options.xml to alert dialog builder
        optionsDialogBuilder = new AlertDialog.Builder(this);
        optionsDialogBuilder.setView(promptsView);

        // Set areas in spinner
        spinnerAreas = (Spinner) promptsView.findViewById(R.id.spinnerAreas);

        // If areas are already loaded
        if (! areas.isEmpty()) {
            spinnerOptions(areas);
            return;
        }

        Call<ZonasResponse> call = HomeSolutionApiAdapter.getApiService().getZonasResponse();
        call.enqueue(new Callback<ZonasResponse>() {
            @Override
            public void onResponse(Response<ZonasResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    // Setting the options in the spinner
                    areas.addAll(response.body().getResponse());
                    spinnerOptions(areas);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerOptions(ArrayList<String> areas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areas);
        spinnerAreas.setAdapter(adapter);
    }

    private void changeArea() {
        showOptionsDialog(etArea.getText().toString());
    }

    private void showOptionsDialog(String initialValue) {
        // Set initial value
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinnerAreas.getAdapter();
        spinnerAreas.setSelection(arrayAdapter.getPosition(initialValue));

        // Set dialog message
        optionsDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String area = spinnerAreas.getSelectedItem().toString();
                                updateArea(area);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // Create alert dialog and show it
        AlertDialog alertDialog = optionsDialogBuilder.create();
        alertDialog.show();
    }

    private void changeEmail() {
        showInputDialog("Nuevo e-mail:", EMAIL);
    }

    private void changeUsername() {
        showInputDialog("Nuevo nombre de usuario:", USERNAME);
    }

    private void changePassword() {
        showInputDialog("Nueva contraseña:", PASSWORD);
    }

    private void showInputDialog(String question, final int target) {
        // Get prompts_input view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts_input, null);

        // Set prompts_input.xml to alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        // Question and default answer
        ((TextView) promptsView.findViewById(R.id.tvQuestion)).setText(question);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.etInput);
        String initialValue;
        switch (target) {
            case EMAIL: initialValue = etEmail.getText().toString(); break;
            case USERNAME: initialValue = etUsername.getText().toString(); break;
            default: initialValue = ""; // PASSWORD
        }
        userInput.setText(initialValue);

        // Set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Get input and apply the changes
                                String input = userInput.getText().toString();
                                switch (target) {
                                    case EMAIL: updateEmail(input); break;
                                    case USERNAME: updateUsername(input); break;
                                    default: updatePassword(input); // PASSWORD
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // Create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /*
    * Webservice requests
    */
    private void updateArea(final String area) {
        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getModificarDatos(token, area, null, null, null, null);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    etArea.setText(area);
                    global.setArea(area);
                }
                else
                    Toast.makeText(UpdateUserActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UpdateUserActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmail(final String email) {
        String username = global.getUsername();
        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getModificarDatos(token, null, null, null, username, email);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    etEmail.setText(email);
                    global.setEmail(email);
                }
                else
                    Toast.makeText(UpdateUserActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UpdateUserActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUsername(final String username) {
        String email = global.getEmail();
        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getModificarDatos(token, null, null, null, username, email);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    etUsername.setText(username);
                    global.setUsername(username);
                }
                else
                    Toast.makeText(UpdateUserActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UpdateUserActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword(final String password) {
        if (password.length() < 6) {
            Toast.makeText(UpdateUserActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }


        final String email = global.getEmail();
        final String username = global.getUsername();
        Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService().getModificarDatos(token, null, null, password, username, email);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    Toast.makeText(UpdateUserActivity.this, "Su contraseña se ha modificado", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(UpdateUserActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UpdateUserActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
