package com.clv.vueling.model;

import java.util.List;

import android.graphics.Bitmap;

public class RowOpenConversation {

	private String title;
	private String subtitle;
	private String url_image;
	private List<String> users;
	private Bitmap image;


	public RowOpenConversation(){}

	public RowOpenConversation(List<String> users,String title,String subtitle,String url_image){
		this.users = users;
		this.title = title;
		this.subtitle = subtitle;
		this.url_image = url_image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getUrl_image() {
		return url_image;
	}

	public void setUrl_image(String url_image) {
		this.url_image = url_image;
	}

	@Override
	public String toString() {
		return "RowOpenConversation [title=" + title + ", subtitle=" + subtitle
				+ ", url_image=" + url_image + "]";
	}

	public List<String> getUsers() {
		return users;
	}

	public void setId_user(List<String> users) {
		this.users = users;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}



	public void setUsers(List<String> users) {
		this.users = users;
	}
}
