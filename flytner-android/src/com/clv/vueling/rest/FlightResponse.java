package com.clv.vueling.rest;

public class FlightResponse {

	private String id;
	private String flyNumber;
	private String origin;
	private String destination;
	
	public FlightResponse() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlyNumber() {
		return flyNumber;
	}

	public void setFlyNumber(String flyNumber) {
		this.flyNumber = flyNumber;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
}
