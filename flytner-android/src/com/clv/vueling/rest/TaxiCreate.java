package com.clv.vueling.rest;

public class TaxiCreate {
	private String address;
	private int numberSit;
	private String userCreate;
	private String fly_id;

	public TaxiCreate() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getNumberSit() {
		return numberSit;
	}

	public void setNumberSit(int numberSit) {
		this.numberSit = numberSit;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public String getFly_id() {
		return fly_id;
	}

	public void setFly_id(String fly_id) {
		this.fly_id = fly_id;
	}

}
