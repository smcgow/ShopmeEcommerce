package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return userRepository.findAll();
	}
	
	public List<Role> listRoles(){
		return roleRepository.findAll();
	}

	public void save(User user) {
		encodePassword(user);
		userRepository.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public Boolean isEmailUnique(String email) {
		User user = userRepository.getUserByEmail(email);
		return user == null;
	}

}
