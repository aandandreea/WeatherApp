import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try {
            // Creating HTTP client
            HttpClient client = HttpClient.newHttpClient();

            //for entering the city we want the weather for
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter city ");
            String city = scanner.nextLine();

            // encode the name of city(without spaces)
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + encodedCity + "&count=1";

            // create geoRequest
            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(geoUrl))
                    .GET()
                    .build();

            // send geoRequest
            HttpResponse<String> geoResponse = client.send(geoRequest,HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode geoRoot = objectMapper.readTree(geoResponse.body());
            JsonNode results = geoRoot.get("results");
            if(results == null || !results.isArray() || results.size() == 0){
                System.out.println("City is not found");
                return;
            }
            if (geoResponse.statusCode() != 200) {
                System.out.println("Error contacting geocoding API");
                return;
            }


            JsonNode firstResult = results.get(0);

            double latitude = firstResult.get("latitude").asDouble();
            double longitude = firstResult.get("longitude").asDouble();

            String weatherUrl = "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&current=temperature_2m";

            HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .GET()
                    .build();

            // send request
            HttpResponse<String> weatherResponse = client.send(weatherRequest,HttpResponse.BodyHandlers.ofString());

            JsonNode weatherRoot = objectMapper.readTree(weatherResponse.body());
            JsonNode current = weatherRoot.get("current");
            double temperature = current.get("temperature_2m").asDouble();

            System.out.println("Temperature in " + city +" is " +  temperature + "°C");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}