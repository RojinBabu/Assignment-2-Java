package com.rojin.flightstatus;

public class Flight {
    private String flightNumber;
    private String airline;
    private String departure;
    private String arrival;
    private String status;

    public Flight(String flightNumber, String airline, String departure, String arrival, String status) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
    }

    // Getters
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Flight: " + flightNumber + " | Airline: " + airline + " | Status: " + status;
    }
}
