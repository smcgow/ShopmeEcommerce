package com.shopme.admin.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	public static final int BRAND_PAGE_SIZE = 4;
	
	@Autowired
	BrandRepository brandRepository;
	
	public Page<Brand> listBrands(Integer pageNumber, String sortDirection){
		Sort sort;
		if(sortDirection.equals("asc")) {
			sort = Sort.by("name").ascending();
		}else {
			sort = Sort.by("name").descending();
		}
		pageNumber = pageNumber - 1;
		PageRequest pageRequest = PageRequest.of(pageNumber, BRAND_PAGE_SIZE, sort);
		
		return brandRepository.findAll(pageRequest);
	}

}
