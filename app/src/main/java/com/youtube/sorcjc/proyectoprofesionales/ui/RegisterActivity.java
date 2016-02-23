package com.youtube.sorcjc.proyectoprofesionales.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.youtube.sorcjc.proyectoprofesionales.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.youtube.sorcjc.proyectoprofesionales.io.HomeSolutionApiAdapter;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.LoginResponse;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.ZonasResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private ProgressDialog progressDialog;

    // Fields
    private EditText etNombre;
    private Spinner spinnerZona;
    private EditText etEmail;
    private EditText etPassword;

    // Actions
    private Button btnRealizarRegistro;
    private TextView btnIrLogin;

    // Social buttons
    private LoginButton btnIngresarFacebook;
    private Button btnIngresarGoogle;

    // Facebook SDK
    private CallbackManager callbackManager;

    // Google API Client
    private GoogleApiClient mGoogleApiClient;
    // Request code used to invoke sign in user interactions
    private static final int RC_SIGN_IN = 0;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // The facebook sdk have to be loaded before
        setContentView(R.layout.activity_register);

        // Get request to load the list
        loadAreasFromWS();

        // View controls
        spinnerZona = (Spinner) findViewById(R.id.spinnerZona);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        // Buttons
        btnRealizarRegistro = (Button) findViewById(R.id.btnRealizarRegistro);
        btnIrLogin = (TextView) findViewById(R.id.btnIrLogin);

        // Button actions
        btnRealizarRegistro.setOnClickListener(this);
        btnIrLogin.setOnClickListener(this);

        // Social buttons
        btnIngresarFacebook = (LoginButton) findViewById(R.id.btnIngresarFacebook);
        btnIngresarGoogle = (Button) findViewById(R.id.btnIngresarGoogle);

        // To manage the login using facebook
        //setUpFacebookLogin();
        // To manage the login using google+
        btnIngresarGoogle.setOnClickListener(this);
        setUpGoogleSignIn();
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

    private void setUpFacebookLogin() {
        final Context context = this;

        btnIngresarFacebook.setReadPermissions(Arrays.asList("email"));
        btnIngresarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                // Credentials are correct, but we have to verify if the e-mail is registered

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Procesando datos ...");
                progressDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("Test/Facebook", "accessToken: " + accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("Test/Facebook", response.toString());

                        // Get facebook data from login
                        Bundle facebookData = getFacebookData(object);
                        final String facebookId = facebookData.getString("id_facebook");
                        final String email = facebookData.getString("email");
                        //final String gcm_id = global.getGcmId();
                        //final String sign = md5(email + facebookId + "ba314mdq");

                        //Call<LoginResponse> call = HomeSolutionApiAdapter.getApiService().getLoginFbResponse(email, facebookId, sign, gcm_id);
                        //call.enqueue(RegisterActivity.this);

                        LoginManager.getInstance().logOut();
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

                    bundle.putString("id_facebook", object.getString("id"));
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

    private void loadAreasFromWS() {
        // We will use retrofit to get the list
        Call<ZonasResponse> call = HomeSolutionApiAdapter.getApiService().getZonasResponse();
        call.enqueue(new Callback<ZonasResponse>() {
            @Override
            public void onResponse(Response<ZonasResponse> response, Retrofit retrofit) {
                if (response.body() != null && response.body().getStatus() == 1)
                {
                    // Setting the options in the spinner
                    ArrayList<String> areas = response.body().getResponse();
                    spinnerOptions(areas);
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

    private void spinnerOptions(ArrayList<String> areas) {
        // Add a default first option
        areas.add(0, "Seleccione zona");
        // Load items on the spinner using an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areas);
        spinnerZona.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRealizarRegistro:
                performNormalRegister();
                break;

            case R.id.btnIngresarFacebook:
                Log.d("Test/Register", "Sign in with fb clicked !");
                break;

            case R.id.btnIngresarGoogle:
                signInWithGoogle();
                break;

            case R.id.btnIrLogin:
                goToLoginActivity();
                break;
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private String getGcmId() {
        final Global global = (Global) getApplicationContext();
        return global.getGcmId();
    }

    private void performNormalRegister() {
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
