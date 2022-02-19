package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void testEncodePassowrod() {
		
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		String rawPassword="nam2020";
		String encodePassword=encoder.encode(rawPassword);
		System.out.println(encodePassword);
		
		boolean matches=encoder.matches(rawPassword, encodePassword);
		assertThat(matches).isTrue();
		
	}

}
