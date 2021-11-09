package com.Project.Project;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "12345";
		String encodedPassword = encoder.encode(rawPassword);
		
		System.out.println(encodedPassword);//$2a$10$c9LC9nLzGzeUjnBTFkTO/.eJAQBhjnwd.m25cd4NAulzfJnpmKBcO
	 }

}
