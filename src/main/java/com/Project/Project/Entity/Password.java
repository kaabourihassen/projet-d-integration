package com.Project.Project.Entity;

public class Password {

	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private Long userId;
	
	public Password() {
		super();
	}

	
	
	public Password(Long userId) {
		super();
		this.userId = userId;
	}



	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isValidPassword() {//= true when newPassword == confirmPassword  
		return this.newPassword.equals(this.confirmPassword);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
