package com.homesolution.app.io.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.homesolution.app.io.response.ChatResponse;
import com.homesolution.app.domain.SimpleWorkerData;
import com.homesolution.app.domain.Talk;

import java.lang.reflect.Type;

public class ChatDeserializer implements JsonDeserializer<ChatResponse> {

    @Override
    public ChatResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Using a custom deserializer for chat WS

        Gson gson = new Gson();
        ChatResponse chatResponse = gson.fromJson(json, ChatResponse.class);

        if (chatResponse.getStatus() == 1) {
            // The full response as a json object
            final JsonObject jsonObject = json.getAsJsonObject();
            // The response attribute in the JSON received
            final JsonElement jsonElement = jsonObject.get("response");

            // The response will be parsed because the status was 1
            Talk talk = gson.fromJson(jsonElement, Talk.class);
            chatResponse.setResponse(talk);

            // Is this user a worker?
            if (talk.isPrestador()) {
                // The response as an object
                final JsonObject responseObject = jsonElement.getAsJsonObject();
                final JsonElement simpleDataElement = responseObject.get("prestador");
                SimpleWorkerData simpleWorkerData = gson.fromJson(simpleDataElement, SimpleWorkerData.class);
                talk.setPrestador(simpleWorkerData);
            }
        }

        return chatResponse;
    }
}
