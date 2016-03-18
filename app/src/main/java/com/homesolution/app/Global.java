package com.homesolution.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.gson.Gson;
import com.homesolution.app.domain.Worker;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.domain.Category;
import com.homesolution.app.domain.UserAuthenticated;

import java.util.ArrayList;

public class Global extends Application {

    /*
    * User authenticated data
    */
    private UserAuthenticated userAuthenticated;

    public boolean isAuthenticated() {
        Log.d("Test/Global", "userAuthenticated is null => " + (userAuthenticated == null));
        return userAuthenticated != null;
    }

    public String getToken() {
        if (userAuthenticated == null) {
            loadUserAuthenticatedFromSharedPreferences();
        }

        return userAuthenticated.getToken();
    }

    public void loadUserAuthenticatedFromSharedPreferences() {
        // SharedPreferences instance
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        // Read the user authentication data from SharedPreferences
        String userData = sharedPref.getString(getString(R.string.user_data), "");

        if (! userData.equals(""))
            setUserAuthenticated(new Gson().fromJson(userData, UserAuthenticated.class));
    }

    public String getUid() {
        return userAuthenticated.getUser().getUid();
    }

    public String getArea() {
        return userAuthenticated.getUser().getExtras().getArea();
    }

    public void setArea(String area) {
        userAuthenticated.getUser().getExtras().setArea(area);
    }

    public String getEmail() {
        return userAuthenticated.getUser().getEmail();
    }

    public void setEmail(String email) {
        userAuthenticated.getUser().setEmail(email);
    }

    public String getUsername() {
        return userAuthenticated.getUser().getUsername();
    }

    public void setUsername(String username) {
        userAuthenticated.getUser().setUsername(username);
    }

    public void setUserAuthenticated(UserAuthenticated userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }


    /*
    * Registration token (GCM Id)
    */
    private static String gcmId;

    public void setGcmId(String registrationToken) {
        gcmId = registrationToken;
    }

    public String getGcmId() {
        return gcmId;
    }


    /*
    * Categories of the selected worker
    */
    private ArrayList<Category> categories;

    // This will be used to score
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    // The categories list just requires the names
    public ArrayList<String> getCategories() {
        ArrayList<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        return categoryNames;
    }

    // Returns the categoryId searching by the name
    public String getCategoryId(String categoryName) {
        for (Category c : categories)
            if (c.getName().equals(categoryName))
                return c.getCid();

        return "-1";
    }


    /*
    * Contacts list
    */
    private static ArrayList<Worker> contacts;

    public void setContacts(ArrayList<Worker> contacts) {
        this.contacts = contacts;
    }

    public boolean isContact(String pId) {
        for (Worker contact : contacts)
            if (contact.getPid().equals(pId))
                return true;

        return false;
    }


    /*
    * Enabling multi-dex
    */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
