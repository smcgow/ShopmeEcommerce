package com.shopme.admin.brand.controller;

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
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listBrands(Model model,
							@RequestParam(name = "keyword", required = false) String keyword,
							@RequestParam(name = "sortDir", required = false) String sortDir) {
		return listBrandsByPage(model, 1, keyword, sortDir);
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create Brand");
		
		List<Category> listCategories = categoryService.listAll("desc");
		model.addAttribute("listCategories", listCategories);
		
		return "brands/brand_form";
		
	}
	
	@GetMapping("/brands/page/{pageNumber}")
	public String listBrandsByPage(Model model,
								@PathVariable(name = "pageNumber") Integer pageNumber,
								@RequestParam(name = "keyword", required = false) String keyword,
								@RequestParam(name = "sortDir", required = false) String sortDir) {
		if(sortDir == null) {
			sortDir = "desc";
		}
		
		Page<Brand> listBrands = brandService.listBrands(pageNumber, sortDir);
		model.addAttribute("listBrands", listBrands.getContent());
		return "brands/brands";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		
		redirectAttributes.addFlashAttribute("message", "The brand was saved successfully");
		
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
		try {
			Brand brand = brandService.get(id);
			List<Category> categories = categoryService.listAll("desc");
			model.addAttribute("listCategories", categories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Edit Brand ID (" + id + ")");
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
		
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(Model model, RedirectAttributes redirectAttributes,
								@PathVariable("id") Integer id) {
		
		try {
			brandService.delete(id);
			String directory = "../brand-logos/" + id;
			FileUploadUtil.removeDirectory(directory);
			redirectAttributes.addFlashAttribute("message", "The brand with ID (" + id + ") was successfully deleted.");
			return "redirect:/brands";
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}
	
	

}
