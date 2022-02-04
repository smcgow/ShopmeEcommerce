package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listCategories(@RequestParam(name = "keyword", required = false) String keyword, 
								@RequestParam(name = "sortDir", required = false) String sortDir, 
								Model model) {
		
		
		return listCategoriesByPage(keyword, sortDir, 1, model);
	}
	
	@GetMapping("/categories/page/{pageNumber}")
	public String listCategoriesByPage(@RequestParam(name = "keyword", required = false) String keyword, 
								@RequestParam(name = "sortDir", required = false) String sortDir, 
								@PathVariable(name = "pageNumber") int pageNumber, 
								Model model) {
		
		List<Category> categories;
		CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
		if(sortDir == null) {
			sortDir = "asc";
		}
		
		if(keyword == null || keyword.isEmpty()) {
			categories = categoryService.listByPage(sortDir,pageNumber, categoryPageInfo);
		}else {
			categories = categoryService.searchByKeyword(pageNumber, keyword, sortDir, categoryPageInfo);
		}
		
		int startCount = ((pageNumber - 1) * CategoryService.CATEGORIES_PER_PAGE) + 1;
		int endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;
		if(endCount > categoryPageInfo.getTotalElements()) {
			endCount = (int) categoryPageInfo.getTotalElements();
		}
		
		model.addAttribute("currentPage",pageNumber);
		model.addAttribute("startCount",startCount);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
		model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
		model.addAttribute("categories", categories);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortDir", sortDir);
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listAll("asc");
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");
		return "categories/category_form";
	}
	
	@GetMapping("/categories/edit/{categoryId}")
	public String editCategory(@PathVariable("categoryId") Integer categoryId, Model model,RedirectAttributes redirectAttributes) {
		List<Category> listCategories = categoryService.listAll("asc");
		Category category;
		try {
			category = categoryService.getCategoryById(categoryId);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category");
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", "Could not find category with ID " + categoryId);
			return "redirect:/categories";
		}
	}
	
	
	@GetMapping("/categories/{categoryId}/enabled/{enableDisable}")
	public String enableDisableCategories(@PathVariable("categoryId") Integer categoryId, 
										@PathVariable("enableDisable") boolean enableDisable,
										Model model,
										RedirectAttributes redirectAttributes) {
		
		try {
			Category category = categoryService.getCategoryById(categoryId);
			categoryService.enableDisableCategory(enableDisable, category.getId());
			if(enableDisable) {
				redirectAttributes.addFlashAttribute("message", "The category has been enabled");
			}else {
				redirectAttributes.addFlashAttribute("message", "The category has been disabled");
			}
			return "redirect:/categories";
		} catch (CategoryNotFoundException e1) {
			redirectAttributes.addFlashAttribute("message", "Could not find the category with ID " + categoryId);
			return "redirect:/categories";
		}
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		
		if(!file.isEmpty()) {
		
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		}else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The category was saved successfully.");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{categoryId}")
	public String deleteCategoryById(@PathVariable("categoryId") Integer categoryId,
										Model model,
										RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(categoryId);
			String directory = "../category-images/" + categoryId;
			FileUploadUtil.removeDirectory(directory);
			
			redirectAttributes.addFlashAttribute("message", "The category ID:" + categoryId + " has been successfully deleted");
		} catch (CategoryNotFoundException e1) {
			redirectAttributes.addFlashAttribute("message", e1.getMessage());
		}
		return "redirect:/categories";
	}

}
