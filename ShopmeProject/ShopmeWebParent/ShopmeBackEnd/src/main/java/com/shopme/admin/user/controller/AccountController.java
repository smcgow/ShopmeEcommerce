package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public String viewAccountDetails(@AuthenticationPrincipal ShopmeUserDetails userDetails, Model model) {
		User user = userService.getUserByEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShopmeUserDetails userDetails) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.updateAccount(user);
			String uploadDirectory = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDirectory(uploadDirectory);
			FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
		}else {
			if(user.getPhotos().isBlank()) {
				user.setPhotos(null);
			}
			userService.updateAccount(user);
		}
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated successfully.");
		return "redirect:/account"; 
	}
}
