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

        public final HttpClient client;
        public final ObjectMapper objectMapper;

    public GeocodingService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public double[] getCoordinates(String city) throws Exception{

        String encodedCity = URLEncoder.encode(city,StandardCharsets.UTF_8);
        String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + encodedCity + "&count=1";

        HttpRequest geoRequest = HttpRequest.newBuilder()
                .uri(URI.create(geoUrl))
                .GET()
                .build();

        HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode geoRoot = objectMapper.readTree(geoResponse.body());
        JsonNode results = geoRoot.get("results");

        if(results == null || !results.isArray() || results.isEmpty()){
            throw new Exception("City was not found");
        }

        JsonNode firstResult = results.get(0);
        double latitude = firstResult.get("latitude").asDouble();
        double longitude = firstResult.get("longitude").asDouble();

        return new double[]{latitude,longitude};
    }
}


