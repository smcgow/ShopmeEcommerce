package com.shopme.admin.brand;

import java.util.NoSuchElementException;

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
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find brand with id " + id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		if(countById == null || countById <= 0) {
			throw new BrandNotFoundException("Could not find brand to delete with id " + id);
		}
		brandRepository.deleteById(id);
	}
	
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	public String checkUnique(Integer id, String name) {
		Brand findByName = brandRepository.findByName(name);
		if(findByName == null) {
			return "OK";
		}else if(id == findByName.getId()) {
			return "OK";
		}else {
			return "Duplicate";
		}
	}
	
	
	

}
