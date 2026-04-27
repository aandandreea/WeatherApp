package org.example.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.*;

import java.util.List;

public class WeatherUI extends Application {

    private final GeocodingService geocodingService = new GeocodingService();
    private final WeatherService weatherService = new WeatherService();

    private TextField cityInput;
    private Label locationLabel;
    private Label temperatureLabel;
    private Label conditionLabel;
    private Label detailsLabel;
    private VBox forecastBox;

    @Override
    public void start(Stage stage) {
        cityInput = new TextField();
        cityInput.setPromptText("Enter city name");
        cityInput.setMaxWidth(260);
        cityInput.setPrefHeight(40);
        cityInput.setStyle("""
                -fx-font-size: 14px;
                -fx-padding: 8;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                """);

        Button searchButton = new Button("Search");
        searchButton.setPrefHeight(40);
        searchButton.setStyle("""
                -fx-font-size: 14px;
                -fx-padding: 8 20;
                -fx-background-radius: 10;
                -fx-cursor: hand;
                """);

        searchButton.setOnAction(e -> searchWeather());
        cityInput.setOnAction(e -> searchWeather());

        HBox searchBox = new HBox(12, cityInput, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setMaxWidth(Region.USE_PREF_SIZE);

        locationLabel = new Label("Search for a city");
        locationLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        temperatureLabel = new Label("--°C");
        temperatureLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold;");

        conditionLabel = new Label("");
        conditionLabel.setStyle("-fx-font-size: 20px;");

        detailsLabel = new Label("");
        detailsLabel.setStyle("-fx-font-size: 16px;");
        detailsLabel.setAlignment(Pos.CENTER);

        Label forecastTitle = new Label("3-Day Forecast");
        forecastTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        forecastBox = new VBox(12);
        forecastBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(22,
                searchBox,
                locationLabel,
                temperatureLabel,
                conditionLabel,
                detailsLabel,
                forecastTitle,
                forecastBox
        );

        root.setAlignment(Pos.CENTER);
        root.setStyle("""
                -fx-padding: 35;
                -fx-background-color: linear-gradient(to bottom right, #74ebd5, #9face6);
                -fx-font-family: Arial;
                """);

        Scene scene = new Scene(root, 650, 700);

        stage.setTitle("WeatherApp");
        stage.setScene(scene);
        stage.show();
    }

    private void searchWeather() {
        String city = cityInput.getText().trim();

        if (city.isEmpty()) {
            locationLabel.setText("Please enter a city");
            temperatureLabel.setText("--°C");
            conditionLabel.setText("");
            detailsLabel.setText("");
            forecastBox.getChildren().clear();
            return;
        }

        try {
            locationLabel.setText("Loading...");
            temperatureLabel.setText("--°C");
            conditionLabel.setText("");
            detailsLabel.setText("");
            forecastBox.getChildren().clear();

            Location location = geocodingService.getCoordinates(city);

            if (location == null) {
                locationLabel.setText("City not found");
                return;
            }

            WeatherData weather = weatherService.getCurrentWeather(
                    location.getLatitude(),
                    location.getLongitude()
            );

            List<ForecastData> forecast = weatherService.getForecast(
                    location.getLatitude(),
                    location.getLongitude()
            );

            locationLabel.setText(location.getName() + ", " + location.getCountry());
            temperatureLabel.setText(weather.getTemperature() + "°C");

            conditionLabel.setText(
                    WeatherCodeMapper.getDescription(weather.getWeatherCode())
            );

            detailsLabel.setText(
                    "Feels like: " + weather.getFeelsLike() + "°C\n"
                            + "Humidity: " + weather.getHumidity() + "%\n"
                            + "Wind: " + weather.getWindSpeed() + " km/h"
            );

            if (forecast != null) {
                for (ForecastData day : forecast) {
                    Label dayLabel = new Label(
                            day.getDate()
                                    + "\nMax: " + day.getMaxTemperature() + "°C"
                                    + "\nMin: " + day.getMinTemperature() + "°C"
                                    + "\n" + WeatherCodeMapper.getDescription(day.getWeatherCode())
                    );

                    dayLabel.setWrapText(true);
                    dayLabel.setMinWidth(300);
                    dayLabel.setMaxWidth(300);
                    dayLabel.setAlignment(Pos.CENTER);

                    dayLabel.setStyle("""
                            -fx-background-color: rgba(255,255,255,0.35);
                            -fx-padding: 14;
                            -fx-background-radius: 14;
                            -fx-font-size: 14px;
                            """);

                    forecastBox.getChildren().add(dayLabel);
                }
            }

        } catch (Exception e) {
            locationLabel.setText("Something went wrong");
            detailsLabel.setText(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}