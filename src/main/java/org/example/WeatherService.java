package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    public WeatherData getCurrentWeather(double latitude, double longitude) {
        try {
            String url = BASE_URL +
                    "?latitude=" + latitude +
                    "&longitude=" + longitude +
                    "&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode current = root.get("current");

            if (current == null) {
                System.out.println("Weather data not available");
                return null;
            }
            double temperature = current.get("temperature_2m").asDouble();
            int humidity = current.get("relative_humidity_2m").asInt();
            double feelsLike = current.get("apparent_temperature").asDouble();
            int weatherCode = current.get("weather_code").asInt();
            double windSpeed = current.get("wind_speed_10m").asDouble();
            String time = current.get("time").asText();

            return new WeatherData(temperature, windSpeed, humidity, feelsLike, weatherCode, time);
        } catch(Exception e){
            System.out.println("Error getting weather " + e.getMessage());
            return null;
        }
    }
}