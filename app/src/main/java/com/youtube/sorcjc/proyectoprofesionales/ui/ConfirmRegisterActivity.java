package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.RegistroResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ConfirmRegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private TextView tvTermsAndConditions;

    // New user data
    private String nombre;
    private String zona;
    private String email;
    private String password;
    private String gcm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);

        // Getting an action bar instance
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTermsAndConditions();

        // Getting the parameters from register activity
        Bundle b = getIntent().getExtras();
        nombre = b.getString("nombre");
        zona = b.getString("zona");
        email = b.getString("email");
        password = b.getString("password");
        gcm_id = b.getString("gcm_id");
    }

    private void setTermsAndConditions() {
        tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id dolor pellentesque, sollicitudin risus eget, eleifend ante. Fusce lacinia mi quis arcu accumsan elementum. Phasellus tempus mi nec ligula mollis, sed luctus diam tincidunt. Nam iaculis vulputate nunc vel aliquet. Vivamus molestie convallis dapibus. Duis eu blandit nulla. Donec luctus tellus sit amet iaculis interdum. Sed porta tellus ac massa fermentum, at pellentesque diam aliquam. Phasellus dignissim, leo ac dignissim placerat, libero sapien scelerisque libero, venenatis dapibus ipsum tellus ut orci. Mauris tempus erat sit amet metus finibus fringilla. Vestibulum ante dolor, eleifend et mauris vitae, ornare semper tortor. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer convallis neque id finibus pulvinar. Integer efficitur auctor ex, sit amet malesuada ante tincidunt malesuada. Aliquam consectetur, neque id blandit congue, leo magna fermentum leo, vel maximus lorem est eget purus. Aenean laoreet at tortor nec porttitor.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fullscreen_dialog_register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_save:
                callRegisterWS();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callRegisterWS() {
        Call<RegistroResponse> call = HomeSolutionApiAdapter.getApiService().getRegistroResponse(nombre, email, password, 1, zona, gcm_id);
        call.enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Response<RegistroResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 0) {
                        Toast.makeText(getBaseContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                        onBackPressed();
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando registro ...");
        progressDialog.show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
