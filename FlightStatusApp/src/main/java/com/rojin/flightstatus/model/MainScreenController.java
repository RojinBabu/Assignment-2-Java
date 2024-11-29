package com.rojin.flightstatus;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainScreenController {

    @FXML
    private ImageView planeImage;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> flightListView;

    @FXML
    private Button closeButton;

    @FXML
    private Label flightDetailsLabel;

    private static final String API_KEY = "63841875fa696ed14580997da3eb7b93"; // Your API key
    private List<Flight> flightData; // Store flight data for reference

    @FXML
    public void initialize() {
        // Set the plane image
        planeImage.setImage(new Image("file:plane.png"));

        // Start Button Action
        startButton.setOnAction(e -> openSearchScreen());

        // Search Button Action
        searchButton.setOnAction(e -> {
            try {
                flightData = getAvailableFlights(); // Fetch and store flights
                if (flightData != null && !flightData.isEmpty()) {
                    flightListView.getItems().clear();
                    for (Flight flight : flightData) {
                        flightListView.getItems().add("Flight: " + flight.flight_iata + " | Airline: " +
                                (flight.airline != null ? flight.airline.name : "N/A"));
                    }
                } else {
                    flightListView.getItems().clear();
                    flightListView.getItems().add("No available flights found.");
                }
            } catch (Exception ex) {
                flightListView.getItems().clear();
                flightListView.getItems().add("Error fetching flight details.");
                ex.printStackTrace();
            }
        });

        // Back Button Action
        backButton.setOnAction(e -> openHomeScreen());

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

        // Close button action
        closeButton.setOnAction(e -> closeFlightDetailsWindow());
    }

    // Method to open the Home screen
    private void openHomeScreen() {
        // Logic to switch to home screen
    }

    // Method to open the Search screen
    private void openSearchScreen() {
        // Logic to switch to search screen
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
        flightDetailsLabel.setText(flight.toString());
        // Logic to open Flight Details screen
    }

    // Close the Flight Details window
    private void closeFlightDetailsWindow() {
        // Logic to close the details window
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
