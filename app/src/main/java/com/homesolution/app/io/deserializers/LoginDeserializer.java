package com.homesolution.app.io.deserializers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.homesolution.app.io.responses.LoginResponse;
import com.homesolution.app.domain.UserAuthenticated;

import java.lang.reflect.Type;

public class LoginDeserializer implements JsonDeserializer<LoginResponse> {

    @Override
    public LoginResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.d("Test/Deserializer", "Using a custom deserializer for Login WS");

        Gson gson = new Gson();
        LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);

        if (loginResponse.getStatus() == 1) {
            // The full response as a json object
            final JsonObject jsonObject = json.getAsJsonObject();
            // The response attribute in the JSON received
            final JsonElement jsonElement = jsonObject.get("response");

            // The response will be parsed because the status was 1
            UserAuthenticated userAuthenticated = gson.fromJson(jsonElement, UserAuthenticated.class);
            loginResponse.setResponse(userAuthenticated);
        }

        return loginResponse;
    }
}
