package com.example.projectmvc.service;

import com.example.projectmvc.model.AboutUs;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JsonService {

    private static final String ABOUT_US_URL =
            "https://raw.githubusercontent.com/tahminaakter163001-dev/CoffeeFlow/main/src/main/resources/json/about-us.json";

    public static AboutUs getAboutUs() {

        try {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ABOUT_US_URL))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                Gson gson = new Gson();

                return gson.fromJson(response.body(), AboutUs.class);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}