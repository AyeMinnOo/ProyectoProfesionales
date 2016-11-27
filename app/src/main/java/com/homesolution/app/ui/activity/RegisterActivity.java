package com.homesolution.app.ui.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.homesolution.app.Global;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.LoginResponse;
import com.homesolution.app.io.response.ZonasResponse;
import com.homesolution.app.ui.LoginActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.UserAuthenticated;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, Callback<LoginResponse> {

    // Dialogs
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
    private Button btnIngresarFacebook;
    private Button btnIngresarGoogle;

    // Facebook SDK
    private CallbackManager callbackManager;

    // Google API Client
    private GoogleApiClient mGoogleApiClient;
    // Request code used to invoke sign in user interactions
    private static final int RC_SIGN_IN = 0;

    @TargetApi(16)
    @Override
    public void onBackPressed() {
        final int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= 16) { // API 16
            finishAffinity();
        } else {
            finish();
        }
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
        btnIngresarFacebook = (Button) findViewById(R.id.btnIngresarFacebook);
        btnIngresarGoogle = (Button) findViewById(R.id.btnIngresarGoogle);

        // To manage the login using facebook
        btnIngresarFacebook.setOnClickListener(this);
        setUpFacebookLogin();

        // To manage the login using google+
        btnIngresarGoogle.setOnClickListener(this);
        setUpGoogleSignIn();

        // Custom action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar_guess);
        }
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

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Procesando datos ...");
                progressDialog.show();

                final String accessToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("Test/Facebook", response.toString());

                        // Facebook connect WS
                        final Global global = (Global) getApplicationContext();
                        final String gcmId = global.getGcmId();
                        final String area = spinnerZona.getSelectedItem().toString();
                        Call<LoginResponse> call = HomeSolutionApiAdapter.getApiService(global.getCountry()).getFbConnect(accessToken, gcmId, area);

                        call.enqueue(RegisterActivity.this);

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
        final Global global = (Global) getApplicationContext();
        Call<ZonasResponse> call = HomeSolutionApiAdapter.getApiService(global.getCountry()).getZonasResponse();
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
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();
    }

    private void spinnerOptions(ArrayList<String> areas) {
        // Add a default first option
        areas.add(0, getResources().getString(R.string.default_area));

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
                if (spinnerZona.getSelectedItem().toString().equals(getResources().getString(R.string.default_area))) {
                    Toast.makeText(RegisterActivity.this, "Por favor seleccione una zona", Toast.LENGTH_SHORT).show();
                    return;
                }

                // We have to request read permissions (email requires)
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
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
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String zona = spinnerZona.getSelectedItem().toString();
        final String gcm_id = getGcmId();

        if (nombre.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || zona.equals(getResources().getString(R.string.default_area))) {
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

    @Override
    public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
        progressDialog.dismiss();

        if (response.body() != null && response.body().getStatus() == 1) {
            UserAuthenticated userAuthenticated = response.body().getResponse();
            saveUserData(userAuthenticated);
            goToActivity(PanelActivity.class);
        } else {
            Toast.makeText(this, response.body().getError(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        progressDialog.dismiss();

        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.d("Test/Login", "Login WS onFailure => " + t.getLocalizedMessage());
    }

    private void saveUserData(UserAuthenticated userAuthenticated) {
        // Save the session in a global variable
        final Global global = (Global) getApplicationContext();
        global.setUserAuthenticated(userAuthenticated);

        updateSharedPreferences(userAuthenticated);
    }

    private void updateSharedPreferences(UserAuthenticated userAuthenticated) {
        // Using Shared Preferences to avoid exceptions when memory is clean
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Start app with this activity
        editor.putString(getString(R.string.first_activity), ".ui.LoginActivity");

        // Save the user data in json format
        String userData = new Gson().toJson(userAuthenticated);
        editor.putString(getString(R.string.user_data), userData);

        editor.apply();
    }

    private void goToActivity(Class activity) {
        Intent intent = null;
        intent = new Intent(this, activity);
        if (intent != null)
            startActivity(intent);
    }
}
