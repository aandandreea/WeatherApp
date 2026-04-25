package org.example;

public class ForecastData {

    private final String date;
    private final double maxTemperature;
    private final double minTemperature;
    private final int weatherCode;

    public ForecastData(String date, double maxTemperature, double minTemperature, int weatherCode) {
        this.date = date;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.weatherCode = weatherCode;
    }

    public String getDate() {
        return date;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public int getWeatherCode() {
        return weatherCode;
    }
}
