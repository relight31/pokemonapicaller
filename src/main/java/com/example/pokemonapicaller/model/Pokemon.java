package com.example.pokemonapicaller.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Pokemon {
    private String name;
    private List<String> images = new LinkedList<>();

    private static String[] IMAGES = { "sprites", "versions", "generation-i", "red-blue" };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String img) {
        this.images.add(img);
    }

    public static Pokemon create(String json) throws IOException {
        Pokemon p = new Pokemon();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            p.setName(data.getString("name"));
            // go through each layer of the json object
            for (String i : IMAGES) {
                data = data.getJsonObject(i);
            }
            List<String> l = data.values().stream()
                    .filter(v -> {
                        return v != null;
                    })
                    .map(v -> {
                        return v.toString();
                    })
                    .toList();
            p.setImages(l);
            return p;
        }
    }
}
