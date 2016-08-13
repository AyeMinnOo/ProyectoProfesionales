package com.homesolution.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.homesolution.app.Global;
import com.homesolution.app.io.gcm.QuickstartPreferences;
import com.homesolution.app.io.gcm.RegistrationIntentService;
import com.homesolution.app.ui.activity.PanelActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
            info = getPackageManager().getPackageInfo("com.homesolution.app", PackageManager.GET_SIGNATURES);
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
        }
        */

        // When the internet is not available
        Button btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(this);

        setupGCM();
    }

    private void setupGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Log.d("Test/Main", "Event onReceive fired !");
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
            // Log.d("Test/Main", "Start IntentService to register this application with GCM");
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
        global.loadCountryFromSharedPreferences();

        // There is a session active? Go to the main Panel
        if (global.isAuthenticated()) {
            Intent iPanel = new Intent(this, PanelActivity.class);
            startActivity(iPanel);
            return;
        }

        // Show a dialog with countries when is needed
        String country = global.getCountry();
        if (country.isEmpty())
            createRadioListDialog().show();
        else goToDefaultActivity();

    }

    private void goToDefaultActivity() {
        // Read the default first activity from SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        String defaultActivity = getResources().getString(R.string.first_activity_default);
        String firstActivity = sharedPref.getString(getString(R.string.first_activity), defaultActivity);

        // Start activity
        Intent intent = new Intent();
        intent.setClassName(this, this.getPackageName() + firstActivity);
        startActivity(intent);
    }

    private int getCountryIndex(String countryCode) {
        switch (countryCode) {
            case "ar": return 0;
            case "cl": return 1;
            case "uy": return 2;
            case "ec": return 3;
            case "co": return 4;
            case "pe": return 5;
            default: return 0; // ar
        }
    }

    public AlertDialog createRadioListDialog() {
        // Pre-select a country according to the device
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        if (countryCode == null || countryCode.isEmpty())
            countryCode = tm.getNetworkCountryIso();
        final int preSelectIndex = getCountryIndex(countryCode);

        // Start building the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence[] items = new CharSequence[6];
        items[0] = getString(R.string.country_ar);
        items[1] = getString(R.string.country_cl);
        items[2] = getString(R.string.country_uy);
        items[3] = getString(R.string.country_ec);
        items[4] = getString(R.string.country_co);
        items[5] = getString(R.string.country_pe);

        builder.setTitle(R.string.country_dialog_title)
                .setSingleChoiceItems(items, preSelectIndex, null) // No listener for selection
                .setPositiveButton(R.string.country_dialog_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        saveSelectedCountry(lw.getCheckedItemPosition());
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        goToDefaultActivity();
                    }
                });

        return builder.create();
    }

    private void saveSelectedCountry(int country) {
        String countryCode = "";
        switch (country) {
            case 0: countryCode = "ar"; break;
            case 1: countryCode = "cl"; break;
            case 2: countryCode = "uy"; break;
            case 3: countryCode = "ec"; break;
            case 4: countryCode = "co"; break;
            case 5: countryCode = "pe"; break;
        }

        // Save the selected country as global variable
        final Global global = (Global) getApplicationContext();
        global.setCountry(countryCode);

        // And update the SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.country_data), countryCode);
        editor.apply();

        Toast.makeText(MainActivity.this, "Luego de iniciar sesión puede modifcar su selección", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        // Test internet connection
    }
}
