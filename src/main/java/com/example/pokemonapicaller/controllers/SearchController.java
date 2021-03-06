package com.example.pokemonapicaller.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.pokemonapicaller.model.Pokemon;
import com.example.pokemonapicaller.services.PokemonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path = "/search")
public class SearchController {
    Logger logger = Logger.getLogger(SearchController.class.getName());

    @Autowired
    PokemonService pokemonService;

    @GetMapping
    public String searchPokemon(Model model, @RequestParam(name = "pokemon_name") String pokemonName) {
        RestTemplate template = new RestTemplate();
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;
        try {
            ResponseEntity<String> resp = template.getForEntity(url, String.class);
            // confirm found
            try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
                JsonReader reader = Json.createReader(is);
                JsonObject data = reader.readObject();
                String resultName = data.getJsonArray("forms")
                        .getJsonObject(0)
                        .getString("name");
                model.addAttribute("resultName", resultName.toUpperCase());
                List<String> sprites = data.getJsonObject("sprites")
                        .getJsonObject("versions")
                        .getJsonObject("generation-i")
                        .getJsonObject("red-blue")
                        .keySet()
                        .stream()
                        .map((String key) -> {
                            return data.getJsonObject("sprites")
                                    .getJsonObject("versions")
                                    .getJsonObject("generation-i")
                                    .getJsonObject("red-blue")
                                    .getString(key);
                        })
                        .toList();
                model.addAttribute("pictures", sprites);
            } catch (IOException e) {
                // TODO: handle exception
            }
            return "result";
        } catch (HttpClientErrorException e) {
            return "notFound";
        }
    }

    @GetMapping
    public String searchPokemon2(Model model, @RequestParam(name = "pokemon_name") String pokemonName) {
        Optional<Pokemon> optional = pokemonService.findPokemon(pokemonName);
        if (optional.isEmpty()) {
            return "notFound";
        }
        Pokemon pokemon = optional.get();
        model.addAttribute("resultName", pokemon.getName());
        model.addAttribute("pictures", pokemon.getImages());
        return "result";
    }
}
