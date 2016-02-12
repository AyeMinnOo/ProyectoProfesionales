package com.youtube.sorcjc.proyectoprofesionales;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.youtube.sorcjc.proyectoprofesionales.domain.Category;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;
import com.youtube.sorcjc.proyectoprofesionales.domain.Worker;

import java.util.ArrayList;

public class Global extends Application {

    /*
    * User authenticated data
    */
    private UserAuthenticated userAuthenticated;

    public String getToken() {
        return userAuthenticated.getToken();
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
