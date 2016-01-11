package com.youtube.sorcjc.proyectoprofesionales;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Controls
    private Button btnIniciarSesion;
    private LoginButton btnIngresarFacebook;
    private Button btnIngresarGoogle;
    private Button btnRegistrarme;

    // Facebook SDK
    private CallbackManager callbackManager;

    // Data from the webservice
    private String[] zonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // The facebook sdk have to loaded before
        setContentView(R.layout.activity_main);

        // The next code was used to get the hash key (for the facebook login)
        /*
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.youtube.sorcjc.proyectoprofesionales", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/

        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIngresarFacebook = (LoginButton) findViewById(R.id.btnIngresarFacebook);
        btnIngresarGoogle = (Button) findViewById(R.id.btnIngresarGoogle);
        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);

        // To manage the login attempt
        facebookLogin();

        btnIniciarSesion.setOnClickListener(this);
        btnIngresarGoogle.setOnClickListener(this);
        btnRegistrarme.setOnClickListener(this);
    }

    private void facebookLogin() {
        final Context context = this;

        btnIngresarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Credentials are correct, but we have to verify if the e-mail is registered
                Log.e("Test/Facebook", "Datos de acceso correctos.");
                Toast.makeText(context, "Datos de acceso correctos.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                // If the user cancel the login, just back to the main activity
                Log.e("Test/Facebook", "Acceso cancelado por el usuario.");
                Toast.makeText(context, "Acceso cancelado por el usuario.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("Test/Facebook", "Datos de acceso incorrectos.");
                Toast.makeText(context, "Datos de acceso incorrectos.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnIniciarSesion:
                intent = new Intent(this, LoginActivity.class);
                break;

            case R.id.btnIngresarGoogle:
                break;

            case R.id.btnRegistrarme:
                // Getting the list from the webservice
                cargarZonas();
                // Start the RegistroActivity
                intent = new Intent(this, RegistroActivity.class);
                // and send the info to the new activity
                intent.putExtra("zonas", zonas);
                break;
        }

        if (intent != null)
            startActivity(intent);
    }

    private void cargarZonas() {
        // Here we will use retrofit
        zonas = new String[3];
        zonas[0] = "Zona A";
        zonas[1] = "Zona B";
        zonas[2] = "Zona C";
    }

}
