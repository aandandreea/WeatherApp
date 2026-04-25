package org.example;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        GeocodingService geocodingService = new GeocodingService();
        WeatherService weatherService = new WeatherService();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String city = getCityInput(scanner);

            if(city.equalsIgnoreCase("exit")){
                System.out.println("Have a nice day");
                break;
            }
            try {

                Location location = geocodingService.getCoordinates(city);
                if (location == null){
                    System.out.println("City not found.Please try again.");
                    continue;
                }
                WeatherData weather = weatherService.getCurrentWeather(location.getLatitude(),location.getLongitude());
                printWeather(location,weather);

                List<ForecastData> forecast = weatherService.getForecast(location.getLatitude(),location.getLongitude());
                if(forecast != null){
                    printForecast(forecast);
                }



            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printForecast(List<ForecastData> forecastList){
        System.out.println("3-Day Forecast: ");

        for(ForecastData forecast : forecastList){
            String description = WeatherCodeMapper.getDescription(forecast.getWeatherCode());

            System.out.println("Date: " + forecast.getDate());
            System.out.println("Max: " + forecast.getMaxTemperature());
            System.out.println("Min: " + forecast.getMinTemperature());
            System.out.println("Condition: " + description);
            System.out.println();
        }
    }

    public static String getCityInput(Scanner scanner){
        System.out.println("Enter city (or type 'exist') :");
        return scanner.nextLine().trim();
    }

    public static void printWeather(Location loc,WeatherData weather){
        System.out.println("\nWeather in " + loc.getName() + ", " + loc.getCountry());
        System.out.println("Temperature: " + weather.getTemperature() + "°C");
        System.out.println("Feels like: " + weather.getFeelsLike() + "°C");
        System.out.println("Humidity: " + weather.getHumidity() + "%");
        System.out.println("Wind speed: " + weather.getWindSpeed() + " km/h");

        String condition = WeatherCodeMapper.getDescription(weather.getWeatherCode());
        System.out.println("Condition: " + condition);
        System.out.println("Time: " + weather.getTime());
        System.out.println();

    }
}