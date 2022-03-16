package com.example.pokemonapicaller.services;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    private static final String WEATHER_URL = "https://api.openweathermap.org";
    private String apiKey;

    @PostConstruct // called immediately when object is created
    public void init() {
        apiKey = System.getenv("OPEN_WEATHER_MAP");
        /*
         * set API keys or passwords to environment vars and have your app read the env
         * vars. add the env vars in heroku
         */

    }
    /*
     * Autowired methods cannot be called in constructor method, have to put
     * into @PostConstruct eg pulling data from DB when initialising
     */

    public void getWeather(String city) {
        UriComponentsBuilder.fromUriString(WEATHER_URL)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .toUriString();
    }
}
