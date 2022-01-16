package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;

public class ShopmeUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.getUserByEmail(email);
		if(user != null) {
			ShopmeUserDetails userDetails = new ShopmeUserDetails(user);
			return userDetails;
		}
		throw new UsernameNotFoundException("Security could not find shop me user " + email);
	}

}
