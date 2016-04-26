package com.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
	private String username;
	private String password;
	private String token;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public JSONObject toJson() {
		  JSONObject obj = new JSONObject();
		  
		  obj.put("username", username);
		  obj.put("token", token);
		  return obj;
	}
}
