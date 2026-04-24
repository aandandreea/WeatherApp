package org.example;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeocodingService {


    private static final String BASE_URL = "https://geocoding-api.open-meteo.com/v1/search";

    public Location getCoordinates(String city) {

        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = BASE_URL + "?name=" + encodedCity + "&count=1";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.get("results");

            if (results == null || !results.isArray() || results.isEmpty()) {
                throw new Exception("City was not found");
            }

            JsonNode firstResult = results.get(0);

            String name = firstResult.get("name").asText();
            double latitude = firstResult.get("latitude").asDouble();
            double longitude = firstResult.get("longitude").asDouble();
            String country = firstResult.get("country").asText();

            return new Location(name, latitude, longitude, country);

        } catch(Exception e){
            System.out.println("Error getting coordinates" + e.getMessage());
        }
        return null;

    }
}


