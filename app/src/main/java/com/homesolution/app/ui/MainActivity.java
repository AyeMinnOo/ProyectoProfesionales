package com.homesolution.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.homesolution.app.Global;
import com.homesolution.app.io.gcm.QuickstartPreferences;
import com.homesolution.app.io.gcm.RegistrationIntentService;
import com.youtube.sorcjc.proyectoprofesionales.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Controls
    private Button btnTryAgain;

    // GCM Management
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // When the internet is not available
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(this);

        setupGCM();
    }

    private void setupGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Test/Main", "Event onReceive fired !");
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d("Test/Main", getString(R.string.gcm_send_message));
                } else {
                    Log.d("Test/Main", getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.d("Test/Main", "Start IntentService to register this application with GCM");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d("Test/Main", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Global variables instance
        final Global global = (Global) getApplicationContext();

        // Load info from SharedPreferences
        global.loadUserAuthenticatedFromSharedPreferences();

        // There is a session active?
        if (global.isAuthenticated()) {
            Intent iPanel = new Intent(this, PanelActivity.class);
            startActivity(iPanel);
            return;
        }

        // If not, read the default activity from SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        String defaultActivity = getResources().getString(R.string.first_activity_default);
        String firstActivity = sharedPref.getString(getString(R.string.first_activity), defaultActivity);

        // Start activity
        Intent intent = new Intent();
        intent.setClassName(this, this.getPackageName() + firstActivity);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        // Test internet connection
    }
}
