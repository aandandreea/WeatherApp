package org.example;

public class WeatherData {
    private final double temperature;
    private final double windSpeed;
    private final int humidity;
    private final double feelsLike;
    private final int weatherCode;
    private final String time;

    public WeatherData(double temperature, double windSpeed, int humidity,
                       double feelsLike, int weatherCode, String time) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.feelsLike = feelsLike;
        this.weatherCode = weatherCode;
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public String getTime() {
        return time;
    }

}
