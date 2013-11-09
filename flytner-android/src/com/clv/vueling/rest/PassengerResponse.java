package com.clv.vueling.rest;

public class PassengerResponse {

	private String id;
	private String urlImage;
	private String username;
	private String usernameFB;

	public PassengerResponse() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameFB() {
		return usernameFB;
	}

	public void setUsernameFB(String usernameFB) {
		this.usernameFB = usernameFB;
	}

}
