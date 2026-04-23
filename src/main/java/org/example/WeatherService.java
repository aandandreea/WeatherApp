package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public WeatherService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public double getTemperature(double latitude,double longitude) throws Exception{

        String weatherURL = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m";

        HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create(weatherURL))
                .GET()
                .build();

        HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode weatherRoot = objectMapper.readTree(weatherResponse.body());
        JsonNode current = weatherRoot.get("current");

        if(current == null){
            throw new Exception("Weather data is not found");
        }
        return current.get("temperature_2m").asDouble();
    }
}
