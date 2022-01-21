package com.shopme.admin.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listCategories(@RequestParam(required = false) String keyword, Model model) {
		List<Category> categories;
		if(keyword == null) {
			categories = categoryService.listAll();
		}else {
			categories = categoryService.searchByKeyword(keyword);
		}
		model.addAttribute("categories", categories);
		model.addAttribute("keyword", keyword);
		return "categories/categories";
	}

}
