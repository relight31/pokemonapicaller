package com.example.pokemonapicaller.services;

import java.io.IOException;
import java.util.Optional;

import com.example.pokemonapicaller.model.Pokemon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PokemonService {
    private static final String url = "https://pokeapi.co/api/v2/pokemon/";

    public Optional<Pokemon> findPokemon(String name) {
        String searchTerm = url.formatted(name);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.getForEntity(searchTerm, String.class);
        // ResponseEntity throws an Exception for any status code >400!!
        if (resp.getStatusCodeValue() >= 400) {
            return Optional.empty();
        }
        try {
            Pokemon pokemon = Pokemon.create(resp.getBody());
            return Optional.of(pokemon);
        } catch (IOException e) {
            return Optional.empty();
        }

    }
}
