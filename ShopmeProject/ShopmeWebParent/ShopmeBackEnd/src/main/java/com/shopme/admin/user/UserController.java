package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = userService.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		List<Role> listRoles = userService.listRoles();
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New User");
		model.addAttribute("listRoles", listRoles);
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "The user has beeen saved successfully.");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes,
			Model model) {
	
		try {
			User user = userService.getUser(id);
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID : " + id + ")");
			return "user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes,
			Model model) {
	
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user with id " + id + " was successfully deleted.");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}

}
