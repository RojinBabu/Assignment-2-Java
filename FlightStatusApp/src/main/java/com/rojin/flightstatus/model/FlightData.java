package com.rojin.flightstatus.model;

import com.rojin.flightstatus.model.Departure;

public class FlightData {
    private String flightNumber;
    private String airline;
    private Departure departure;  // Use Departure class instead of String
    private String arrival;
    private String status;

    // Getters and setters
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Flight: " + flightNumber + " | Airline: " + airline + " | Status: " + status +
                " | Departure: " + departure.getAirport() + " at " + departure.getDateTime() +
                " | Arrival: " + arrival;
    }
}
