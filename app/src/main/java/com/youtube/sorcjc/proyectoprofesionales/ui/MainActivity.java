package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.youtube.sorcjc.proyectoprofesionales.R;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    // Controls
    private Button btnIniciarSesion;
    private LoginButton btnIngresarFacebook;
    private SignInButton btnIngresarGoogle;
    private Button btnRegistrarme;

    // Facebook SDK
    private CallbackManager callbackManager;

    // Google API Client
    private GoogleApiClient mGoogleApiClient;
    // Request code used to invoke sign in user interactions
    private static final int RC_SIGN_IN = 0;

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
        btnIngresarGoogle = (SignInButton) findViewById(R.id.btnIngresarGoogle);
        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);

        // To manage the login attempt
        facebookLogin();

        btnIniciarSesion.setOnClickListener(this);
        btnIngresarGoogle.setOnClickListener(this);
        btnRegistrarme.setOnClickListener(this);

        setUpGoogleSignIn();

    }

    private void setUpGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void facebookLogin() {
        final Context context = this;

        btnIngresarFacebook.setReadPermissions(Arrays.asList("email"));
        btnIngresarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProgressDialog progressDialog;

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Credentials are correct, but we have to verify if the e-mail is registered

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Procesando datos ...");
                progressDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("Test/Facebook", "accessToken: " + accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("Test/Facebook", response.toString());
                        progressDialog.dismiss(); // dismiss() is better than hide()
                        // Get facebook data from login
                        Bundle facebookData = getFacebookData(object);
                        Log.i("Test/Facebook", "E-mail:" + facebookData.getString("email"));
                        Log.i("Test/Facebook", "Nombres:" + facebookData.getString("first_name"));
                        Log.i("Test/Facebook", "Apellidos:" + facebookData.getString("last_name"));
                    }
                });

                Bundle parameters = new Bundle();
                // Requested parameters
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            private Bundle getFacebookData(JSONObject object) {
                try {
                    Bundle bundle = new Bundle();

                    String id = object.getString("id");
                    bundle.putString("idFacebook", id);

                    if (object.has("first_name"))
                        bundle.putString("first_name", object.getString("first_name"));
                    if (object.has("last_name"))
                        bundle.putString("last_name", object.getString("last_name"));
                    if (object.has("email"))
                        bundle.putString("email", object.getString("email"));
                    if (object.has("gender"))
                        bundle.putString("gender", object.getString("gender"));
                    if (object.has("birthday"))
                        bundle.putString("birthday", object.getString("birthday"));
                    if (object.has("location"))
                        bundle.putString("location", object.getJSONObject("location").getString("name"));

                    return bundle;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onCancel() {
                // If the user cancel the login, just back to the main activity
                Log.e("Test/Facebook", "Acceso cancelado por el usuario.");
                Toast.makeText(context, "Acceso cancelado por el usuario.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(context, e.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Test/Google", "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Test/Google", "getDisplayName(): " + acct.getDisplayName());
            Log.d("Test/Google", "getEmail(): " + acct.getEmail());
            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnIniciarSesion:
                intent = new Intent(this, LoginActivity.class);
                break;

            case R.id.btnIngresarGoogle:
                signInWithGoogle();
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

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void cargarZonas() {
        // Here we will use retrofit
        zonas = new String[3];
        zonas[0] = "Zona A";
        zonas[1] = "Zona B";
        zonas[2] = "Zona C";
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
