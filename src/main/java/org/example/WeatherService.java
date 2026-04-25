package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {

    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    public WeatherData getCurrentWeather(double latitude, double longitude) {
        try {
            String url = BASE_URL +
                    "?latitude=" + latitude +
                    "&longitude=" + longitude +
                    "&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m";


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


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
        } catch (Exception e) {
            System.out.println("Error getting weather " + e.getMessage());
            return null;
        }
    }

    public List<ForecastData> getForecast(double latitude, double longitude) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&daily=weather_code,temperature_2m_max,temperature_2m_min"
                    + "&forecast_days=3"
                    + "&timezone=auto";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.body());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode daily = root.get("daily");

            if (daily == null) {
                System.out.println("Forecast data is not available.");
                return null;
            }

            JsonNode dates = daily.get("time");
            JsonNode maxTemps = daily.get("temperature_2m_max");
            JsonNode minTemps = daily.get("temperature_2m_min");
            JsonNode codes = daily.get("weather_code");

            List<ForecastData> forecastList = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                ForecastData forecast = new ForecastData(
                        dates.get(i).asText(),
                        maxTemps.get(i).asDouble(),
                        minTemps.get(i).asDouble(),
                        codes.get(i).asInt());
                forecastList.add(forecast);
            }

            return forecastList;

        } catch(Exception e){
            System.out.println("Error while getting forecast data" + e.getMessage());
            return null;
        }
    }
}