package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public String listFirstPage(
			 Model model) {
		return listByPage(1, "firstName", "asc", null, model);
	}
	
	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable("pageNumber") int pageNumber,
							 @RequestParam("sortField") String sortField,
							 @RequestParam("sortDir") String sortDirection,
							 @RequestParam(name = "keyword", required = false) String keyword,
							 Model model) {
		
		Page<User> page = userService.listByPage(pageNumber,sortField,sortDirection, keyword);
		List<User> listUsers = page.getContent();
		
		int startCount = ((pageNumber - 1) * UserService.USERS_PER_PAGE) + 1;
		int endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = (int) page.getTotalElements();
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
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
	public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			String uploadDirectory = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDirectory(uploadDirectory);
			FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
		}else {
			if(user.getPhotos().isBlank()) {
				user.setPhotos(null);
			}
			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has beeen saved successfully.");
		return getAffectedUserUrl(user); 
	}

	private String getAffectedUserUrl(User user) {
		String emailPart = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword=" + emailPart;
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
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
												RedirectAttributes redirectAttributes) {
		userService.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user with ID : " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}

}
