package com.rojin.flightstatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIUtility {

    private static final String API_URL = "https://api.aviationstack.com/v1/flights";
    private static final String API_KEY = "63841875fa696ed14580997da3eb7b93"; // Use your actual API key

    public static String getFlightData(String flightNumber) throws Exception {
        String urlString = API_URL + "?access_key=" + API_KEY + "&flight_iata=" + flightNumber;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
