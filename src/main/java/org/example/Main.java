package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            GeocodingService geocodingService = new GeocodingService();
            WeatherService weatherService = new WeatherService();

            System.out.println("Enter the city ");
            String city = scanner.nextLine();

            double[] coordinates = geocodingService.getCoordinates(city);
            double latitude = coordinates[0];
            double longitude = coordinates[1];

            double temperature = weatherService.getTemperature(latitude,longitude);

            System.out.println("Temperature in " + city + " is " + temperature + " °C");
            scanner.close();
        } catch(Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }
}