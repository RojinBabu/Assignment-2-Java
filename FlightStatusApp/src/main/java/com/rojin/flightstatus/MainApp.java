package com.rojin.flightstatus;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainApp extends Application {
    private static final String API_KEY = "63841875fa696ed14580997da3eb7b93"; // Your API key
    private List<Flight> flightData; // Store flight data for reference

    @Override
    public void start(Stage primaryStage) {
        // Home Screen
        VBox homeScreen = new VBox(10);
        homeScreen.setPadding(new Insets(20));
        homeScreen.setStyle("-fx-background-color: linear-gradient(to bottom, #2193b0, #6dd5ed);");

        Label welcomeLabel = new Label("Welcome to the Flight Status App!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        ImageView planeImage = new ImageView(new Image("file:plane.png")); // Replace with your image file
        planeImage.setFitWidth(200);
        planeImage.setPreserveRatio(true);

        Button startButton = new Button("Get Started");
        startButton.setStyle("-fx-font-size: 18px; -fx-background-color: #FF7F50; -fx-text-fill: white; -fx-padding: 10 20;");

        homeScreen.getChildren().addAll(planeImage, welcomeLabel, startButton);
        homeScreen.setAlignment(javafx.geometry.Pos.CENTER);

        Scene homeScene = new Scene(homeScreen, 800, 600);

        // Flight Search Screen
        VBox searchScreen = new VBox(10);
        searchScreen.setPadding(new Insets(20));

        Label instructionLabel = new Label("Available Flights:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> flightListView = new ListView<>(); // ListView to display available flights
        flightListView.setPrefHeight(400);

        Button searchButton = new Button("Show Available Flights");
        searchButton.setStyle("-fx-font-size: 16px; -fx-background-color: #32CD32; -fx-text-fill: white; -fx-padding: 8 16;");

        searchButton.setOnAction(event -> {
            flightListView.getItems().clear();
            try {
                flightData = getAvailableFlights(); // Fetch and store flights
                if (flightData != null && !flightData.isEmpty()) {
                    for (Flight flight : flightData) {
                        flightListView.getItems().add("Flight: " + flight.flight_iata + " | Airline: " +
                                (flight.airline != null ? flight.airline.name : "N/A"));
                    }
                } else {
                    flightListView.getItems().add("No available flights found.");
                }
            } catch (Exception ex) {
                flightListView.getItems().add("Error fetching flight details.");
                ex.printStackTrace();
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #FF4500; -fx-text-fill: white; -fx-padding: 8 16;");
        backButton.setOnAction(e -> primaryStage.setScene(homeScene));

        searchScreen.getChildren().addAll(instructionLabel, searchButton, flightListView, backButton);

        Scene searchScene = new Scene(searchScreen, 800, 600);

        // Start Button Action
        startButton.setOnAction(e -> primaryStage.setScene(searchScene));

        // Add listener to open details on selection
        flightListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && flightData != null) {
                int selectedIndex = flightListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < flightData.size()) {
                    Flight selectedFlight = flightData.get(selectedIndex);
                    showFlightDetailsWindow(selectedFlight);
                }
            }
        });

        // Set up primary stage
        primaryStage.setScene(homeScene);
        primaryStage.setTitle("Flight Status App");
        primaryStage.getIcons().add(new Image("file:icon.png")); // Replace with your icon file
        primaryStage.show();
    }

    // Method to fetch all available flights from the API
    public List<Flight> getAvailableFlights() throws Exception {
        String urlString = "https://api.aviationstack.com/v1/flights?access_key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read the response from the API
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Parse the JSON response into ApiResponse object
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

        // Return the list of available flights
        if (apiResponse != null && apiResponse.data != null) {
            return apiResponse.data;
        } else {
            return null;
        }
    }

    // Show flight details in a new window
    private void showFlightDetailsWindow(Flight flight) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Flight Details - " + flight.flight_iata);

        VBox detailBox = new VBox(10);
        detailBox.setPadding(new Insets(20));

        Label flightDetails = new Label(flight.toString());
        flightDetails.setStyle("-fx-font-size: 14px;");

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 14px;");
        closeButton.setOnAction(e -> detailStage.close());

        detailBox.getChildren().addAll(flightDetails, closeButton);
        Scene detailScene = new Scene(detailBox, 400, 300);

        detailStage.setScene(detailScene);
        detailStage.show();
    }

    // API response and related classes
    private class ApiResponse {
        List<Flight> data;
    }

    private class Flight {
        String flight_iata;
        Airline airline;
        Departure departure;
        Arrival arrival;
        String flight_status;

        @Override
        public String toString() {
            return "Flight: " + flight_iata +
                    "\nAirline: " + (airline != null ? airline.name : "N/A") +
                    "\nStatus: " + flight_status +
                    "\nDeparture: " + (departure != null ? departure.airport : "N/A") +
                    "\nArrival: " + (arrival != null ? arrival.airport : "N/A");
        }
    }

    private class Airline {
        String name;
    }

    private class Departure {
        String airport;
    }

    private class Arrival {
        String airport;
    }
}
