package com.clv.vueling.model;

import java.io.Serializable;

public class Flight implements Serializable {

	public static final String TAG = "flight";
	
	private static final long serialVersionUID = 2020132213106165964L;

	private String destination;
	private String origin;
	private String flightNumber;

	public Flight() {
	}

	public Flight(String destination, String origin, String flightNumber) {
		this.destination = destination;
		this.origin = origin;
		this.flightNumber = flightNumber;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

}
