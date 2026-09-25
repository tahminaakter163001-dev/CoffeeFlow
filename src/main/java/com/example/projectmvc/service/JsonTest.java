package com.example.projectmvc.service;

import com.example.projectmvc.model.AboutUs;

public class JsonTest {

    public static void main(String[] args) {

        AboutUs aboutUs = JsonService.getAboutUs();

        if (aboutUs != null) {

            System.out.println("Title: " + aboutUs.getTitle());
            System.out.println("Description: " + aboutUs.getDescription());
            System.out.println("Version: " + aboutUs.getVersion());
            System.out.println("Contact: " + aboutUs.getContact());

        } else {

            System.out.println("Failed to load About Us information.");
        }
    }
}