package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.LoginResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.RecuperarResponse;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse>, GoogleApiClient.OnConnectionFailedListener {

    private EditText etEmail;
    private EditText etPassword;

    private ProgressDialog progressDialog;

    // View controls
    private Button btnRealizarLogin;
    private LoginButton btnIngresarFacebook;
    private SignInButton btnIngresarGoogle;
    private Button btnRegistrarme;
    private Button btnOlvidePassword;

    // Facebook SDK
    private CallbackManager callbackManager;

    // Google API Client
    private GoogleApiClient mGoogleApiClient;
    // Request code used to invoke sign in user interactions
    private static final int RC_SIGN_IN = 0;

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

        // Initialize the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // The facebook sdk have to be loaded before
        setContentView(R.layout.activity_login);

        // Getting the view controls
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);
        btnRealizarLogin = (Button) findViewById(R.id.btnRealizarLogin);
        btnOlvidePassword = (Button) findViewById(R.id.btnOlvidePassword);

        btnIngresarFacebook = (LoginButton) findViewById(R.id.btnIngresarFacebook);
        btnIngresarGoogle = (SignInButton) findViewById(R.id.btnIngresarGoogle);

        // To manage the login using facebook
        facebookLogin();
        // To manage the login using google+
        btnIngresarGoogle.setOnClickListener(this);
        setUpGoogleSignIn();

        btnRegistrarme.setOnClickListener(this);
        btnRealizarLogin.setOnClickListener(this);
        btnOlvidePassword.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Google+ Login
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // When the Google+ connection fails
        Toast.makeText(this, "Error de conexión.", Toast.LENGTH_SHORT).show();
    }

    private void facebookLogin() {
        final Context context = this;

        btnIngresarFacebook.setReadPermissions(Arrays.asList("email"));
        btnIngresarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProgressDialog progressDialog;

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Credentials are correct, but we have to verify if the e-mail is registered

                progressDialog = new ProgressDialog(LoginActivity.this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarLogin:
                validarLogin();
                break;
            case R.id.btnIngresarGoogle:
                signInWithGoogle();
                break;
            case R.id.btnRegistrarme:
                goToActivity(RegisterActivity.class);
                break;
            case R.id.btnOlvidePassword:
                recoverPassword();
                break;
        }
    }

    private void recoverPassword() {
        String email = etEmail.getText().toString();
        if (isValidEmail(email)) {
            Call<RecuperarResponse> call = HomeSolutionApiAdapter.getApiService().getRecuperarContra(email);
            call.enqueue(new Callback<RecuperarResponse>() {
                @Override
                public void onResponse(Response<RecuperarResponse> response, Retrofit retrofit) {
                    if (response.body() == null) return;

                    if (response.body().getStatus() == 0)
                        Toast.makeText(LoginActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "Se han enviado las instrucciones a su correo", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Ingrese un e-mail con formato válido", Toast.LENGTH_SHORT).show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            saveUserData(userAuthenticated);
            goToActivity(PanelActivity.class);
            clearCredentials();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        progressDialog.dismiss();
        // The parser fails when the credentials are incorrect
        // It happens because the WS doesn't return a valid object
        Toast.makeText(this, "Sus datos no son correctos", Toast.LENGTH_SHORT).show();
    }

    private void saveUserData(UserAuthenticated userAuthenticated) {
        // Save the session
        final Global global = (Global) getApplicationContext();
        global.setUserAuthenticated(userAuthenticated);
        // Save the sharedPreference
        setNewFirstActivity();

        Log.i("Test/Login", "token => " + userAuthenticated.getToken());
    }

    private void setNewFirstActivity() {
        // Write to Shared Preferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.first_activity), ".ui.LoginActivity");
        Log.i("Test/Login", "SharedPreferences to => .ui.LoginActivity");
        editor.apply();
    }

    private void goToActivity(Class activity) {
        Intent intent = null;
        intent = new Intent(this, activity);
        if (intent != null)
            startActivity(intent);
    }

    private void clearCredentials() {
        etPassword.setText("");
    }
}
