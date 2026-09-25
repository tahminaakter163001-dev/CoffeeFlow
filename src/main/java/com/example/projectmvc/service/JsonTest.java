package com.example.projectmvc.service;

import com.example.projectmvc.model.ApiUser;
import com.google.gson.Gson;

public class JsonTest {

    public static void main(String[] args) {

        String url = "https://jsonplaceholder.typicode.com/users/1";

        String json = JsonService.getJsonFromUrl(url);

        System.out.println("JSON Response:");
        System.out.println(json);

        Gson gson = new Gson();

        ApiUser user = gson.fromJson(json, ApiUser.class);

        System.out.println("\nConverted Java Object:");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
    }
}