package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("w3akPa55w0rd");
		System.out.println(encodedPassword);
		boolean matches = passwordEncoder.matches("w3akPa55w0rd", encodedPassword);
		assertThat(matches).isTrue();
	}

}
