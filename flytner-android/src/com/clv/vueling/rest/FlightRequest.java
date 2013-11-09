package com.clv.vueling.rest;

public class FlightRequest {

	private String flyNumber;
	private String user_id;

	public FlightRequest() {
	}

	public String getFlyNumber() {
		return flyNumber;
	}

	public void setFlyNumber(String flyNumber) {
		this.flyNumber = flyNumber;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
