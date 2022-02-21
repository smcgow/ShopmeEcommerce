package com.shopme.admin.brand.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;

@Controller
public class BrandController {
	
	@Autowired
	BrandService brandService;
	
	@GetMapping("/brands")
	public String listBrands(Model model,
							@RequestParam(name = "keyword", required = false) String keyword,
							@RequestParam(name = "sortDir", required = false) String sortDir) {
		return listBrandsByPage(model, 1, keyword, sortDir);
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

}
