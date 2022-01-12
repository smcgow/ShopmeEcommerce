package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
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
		
		Boolean isUpdate = user.getId() != null;
		
		if(isUpdate) {
			if(user.getPassword().isEmpty()) {
				User existingUser = userRepository.getById(user.getId());
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user);
			}
		}else {
			encodePassword(user);
		}
		
		userRepository.save(user);
	}
	
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public Boolean isEmailUnique(Integer id, String email) {
		User user = userRepository.getUserByEmail(email);
		
		/* 
		 * If user is not found email is unique.
		 * Else If no id it means we are creating so not unique if user found.
		 * Else if the id does exist we are editing and if it's not the same user we block the edit if same email address.
		 * Else it's the same user editing their data hence we allow the update by returning true.
		 */
		if(user == null) {
			return true;
		}else if(id == null) {
			return false;
		}else if(!user.getId().equals(id)){
			return false;
		}else {
			return true;
		}
	}

	public User getUser(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if(countById == null || countById == 0L) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		userRepository.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateUserEnabledStatus(id, enabled);
	}	
	
}
