package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateUserRequest {

	@JsonProperty
	@NotNull(message = "Password cannot be empty")
	private String username;

	@JsonProperty
	@NotNull(message = "Password cannot be empty")
	private String password;

	@JsonProperty
	@NotNull(message = "Password confirmation cannot be empty")
	private String confirmPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
