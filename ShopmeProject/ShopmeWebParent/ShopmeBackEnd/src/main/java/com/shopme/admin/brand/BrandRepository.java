package com.shopme.admin.brand;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

	Brand findByName(String name);
	
	Long countById(Integer id);
	
}
