package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@Slf4j
public class BrandRepositoryTests {
	
	
	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Test
	public void testCreateThreeBrands() {
		
		Category laptops = categoryRepository.findByName("laptops");
		assertThat(laptops).isNotNull();
		
		Category cellPhones = categoryRepository.findByName("Cell Phones & Accessories");
		assertThat(cellPhones).isNotNull();
		
		Category tablets = categoryRepository.findByName("Tablets");
		assertThat(tablets).isNotNull();
		
		Category memory = categoryRepository.findByName("Memory");
		assertThat(memory).isNotNull();
		
		Category internalHardDrives = categoryRepository.findByName("Internal Hard Drives");
		assertThat(internalHardDrives).isNotNull();
		
		Brand acer = Brand.builder()
							.name("Acer")
							.logo("brand-logo.png")
							.build();
		
		acer.getCategories().add(laptops);
		Brand savedAcer = brandRepository.save(acer);
		assertThat(savedAcer.getId()).isGreaterThan(0);
		
		Brand apple = Brand.builder()
				.name("Apple")
				.logo("brand-logo.png")
				.build();

		apple.getCategories().add(cellPhones);
		apple.getCategories().add(tablets);
		Brand savedApple = brandRepository.save(apple);
		assertThat(savedApple.getId()).isGreaterThan(0);
		
		Brand samsung = Brand.builder()
				.name("Samsung")
				.logo("brand-logo.png")
				.build();

		samsung.getCategories().add(memory);
		samsung.getCategories().add(internalHardDrives);
		Brand savedSamsung = brandRepository.save(samsung);
		assertThat(savedSamsung.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testFindAllBrands() {
		
		List<Brand> brands = brandRepository.findAll();
		brands.forEach(brand -> {
			log.info(brand.toString());
		});
	}
	
	@Test
	public void testGetBrandById() {
		
		Optional<Brand> brand =	brandRepository.findById(1);
		assertThat(brand.isPresent()).isTrue();
		
	}
	
	@Test
	public void testUpdateBrand() {
		
		Brand samsung = brandRepository.findByName("Samsung");
		assertThat(samsung).isNotNull();
		
		samsung.setName("Samsung Electronics");
		brandRepository.save(samsung);
		
	}
	
	@Test
	public void testDeleteBrand() {
		
		Brand apple = brandRepository.findByName("Apple");
		assertThat(apple).isNotNull();
		
		
		brandRepository.delete(apple);
		
	}
	

}
