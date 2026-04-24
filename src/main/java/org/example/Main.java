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

            Location location = geocodingService.getCoordinates(city);
            if(location == null) return;
            double lat = location.getLatitude();
            double lon = location.getLongitude();

           WeatherData weather = weatherService.getCurrentWeather(lat,lon);
           if(weather == null) {
               return;
           }

            System.out.println("Temperature: " + weather.getTemperature());
            System.out.println("Feels like: " + weather.getFeelsLike());
            System.out.println("Humidity: " + weather.getHumidity());
            System.out.println("Wind speed: " + weather.getWindSpeed());
            System.out.println("Time: " + weather.getTime());

            String description = WeatherCodeMapper.getDescription(weather.getWeatherCode());
            System.out.println("Condition: " + description);
            scanner.close();
        } catch(Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }
}