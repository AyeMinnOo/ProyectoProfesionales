package com.homesolution.app.io.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.homesolution.app.Global;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.io.response.SimpleResponse;
import com.youtube.sorcjc.proyectoprofesionales.R;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    private static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int UPDATE_INTERVAL_IN_SECONDS = 30 * 60; // 30 minutes
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private HandlerThread handlerThread;
    private Looper looper;

    private boolean alreadyRunning;

    @Override
    public void onCreate()
    {
        super.onCreate();
        alreadyRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // To avoid run the following sentences several times
        if (alreadyRunning) {
            return super.onStartCommand(intent, flags, startId);
        }

        // When the service is started, the first time
        alreadyRunning = true;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();
        // Log.i(TAG, "onStartCommand => locationManager and locationListener created");

        // Creating handlerThread and looper
        handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        looper = handlerThread.getLooper();

        // At the start
        if (isGpsDisabled()) {
            processStartNotification();
        }

        // Requesting location updates
        try {
            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, locationListener, looper);

            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, 0, locationListener, looper);
        } catch (SecurityException ex) {
            Toast.makeText(TrackingService.this, R.string.settings_geo_exception_activating, Toast.LENGTH_SHORT).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void processStartNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("GPS Desactivado")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText("Por favor habilite el GPS para una mejor experiencia")
                .setSmallIcon(R.drawable.ic_stat_ic_notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.i(TAG, "onDestroy => removeUpdates");

        try {
            locationManager.removeUpdates(locationListener);
            looper.quit();
            handlerThread.quit();
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isGpsDisabled() {
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if (isGpsDisabled()) {
                processStartNotification();
                return;
            }

            final String latitude = String.valueOf(loc.getLatitude());
            final String longitude = String.valueOf(loc.getLongitude());
            // Log.i(TAG, "locationListener onLocationChanged => " + latitude + " " + longitude);

            // Perform network I/O
            Global global = (Global) getApplicationContext();
            final String token = global.getToken();
            Call<SimpleResponse> call = HomeSolutionApiAdapter.getApiService(global.getCountry())
                    .getLatLng(token, latitude, longitude);

            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Response<SimpleResponse> response, Retrofit retrofit) {
                    boolean okResponse = response.body().getResponse();
                    if (okResponse)
                        Toast.makeText(TrackingService.this, R.string.settings_geo_activated, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(TrackingService.this, R.string.retrofit_failure, Toast.LENGTH_SHORT).show();
                    // Log.e(TAG, "trackingService onFailure => " + t.getLocalizedMessage());
                }
            });

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    } // end of MyLocationListener

}
