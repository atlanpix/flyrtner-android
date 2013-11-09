package com.clv.vueling.rest;

public class LoginRequest {
	private String idFacebook;
	private String email;
	private String usernameFB;
	private String oauthTokenFB;
	private String urlImage;
	private String name;
	private String regId;
	private String systemPhone;

	public LoginRequest() {
	}

	public String getIdFacebook() {
		return idFacebook;
	}

	public void setIdFacebook(String idFacebook) {
		this.idFacebook = idFacebook;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsernameFB() {
		return usernameFB;
	}

	public void setUsernameFB(String usernameFB) {
		this.usernameFB = usernameFB;
	}

	public String getOauthTokenFB() {
		return oauthTokenFB;
	}

	public void setOauthTokenFB(String oauthTokenFB) {
		this.oauthTokenFB = oauthTokenFB;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getSystemPhone() {
		return systemPhone;
	}

	public void setSystemPhone(String systemPhone) {
		this.systemPhone = systemPhone;
	}

}
