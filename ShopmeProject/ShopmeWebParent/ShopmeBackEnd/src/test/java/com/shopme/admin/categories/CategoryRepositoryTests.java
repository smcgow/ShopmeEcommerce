package com.shopme.admin.categories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@Slf4j
public class CategoryRepositoryTests {
	
	@Autowired CategoryRepository categoryRepository;
	
	@Test
	public void testCreateParentAndSubCategories() {
		
		Category electronics = Category.builder()
				.enabled(true)
				.alias("elec")
				.image("default.png")
				.name("Electronics")
				.build();

		Category parentCategory = categoryRepository.save(electronics);
		assertThat(parentCategory.getId()).isNotNull();
		
		Category category = Category.builder()
				.enabled(true)
				.alias("comp")
				.image("default.png")
				.name("Computers")
				.build();
		
		parentCategory = categoryRepository.save(category);
		assertThat(parentCategory.getId()).isNotNull();
		
		Category desktops = Category.builder()
				.enabled(true)
				.alias("desk")
				.image("default.png")
				.name("Desktops")
				.parent(parentCategory)
				.build();
		
		Category laptops = Category.builder()
				.enabled(true)
				.alias("lap")
				.image("default.png")
				.name("Laptops")
				.parent(parentCategory)
				.build();
		
		Category components = Category.builder()
				.enabled(true)
				.alias("comp")
				.image("default.png")
				.name("Computer Components")
				.parent(parentCategory)
				.build();
		
		List<Category> categories = categoryRepository.saveAll(List.of(laptops,components,desktops));
		assertThat(categories.size()).isEqualTo(3);
		assertThat(categories.get(0).getId()).isNotNull();
		assertThat(categories.get(1).getId()).isNotNull();
		assertThat(categories.get(2).getId()).isNotNull();
		
		Category electrionics = categoryRepository.findByName("Electronics");
		
		Category phones = Category.builder()
				.enabled(true)
				.alias("phone")
				.image("default.png")
				.name("Smart Phones")
				.parent(electrionics)
				.build();
		
		Category cameras = Category.builder()
				.enabled(true)
				.alias("cam")
				.image("default.png")
				.name("Cameras")
				.parent(electrionics)
				.build();
		
		
		categories = categoryRepository.saveAll(List.of(cameras,phones));
		assertThat(categories.size()).isEqualTo(2);
		assertThat(categories.get(0).getId()).isNotNull();
		assertThat(categories.get(1).getId()).isNotNull();
		
		components = categoryRepository.findByName("Computer Components");
		
		Category memory = Category.builder()
				.enabled(true)
				.alias("mem")
				.image("default.png")
				.name("Memory")
				.parent(components)
				.build();
		
		Category savedMemory = categoryRepository.save(memory);
		assertThat(savedMemory.getId()).isNotNull();
		
		laptops = categoryRepository.findByName("Laptops");
		
		Category gaming = Category.builder()
				.enabled(true)
				.alias("gam")
				.image("default.png")
				.name("Gaming Laptop")
				.parent(laptops)
				.build();
		
		Category savedGaming = categoryRepository.save(gaming);
		assertThat(savedGaming.getId()).isNotNull();
		
		Category smart = categoryRepository.findByName("Smart Phones");
		
		Category iPhone = Category.builder()
				.enabled(true)
				.alias("iphone")
				.image("default.png")
				.name("IPhone")
				.parent(smart)
				.build();
		
		Category savedIPhone = categoryRepository.save(iPhone);
		assertThat(savedIPhone.getId()).isNotNull();
	
		
		
	}
	
	@Test
	public void testGetCategoryAndChildren() {
		
		Category electronics = categoryRepository.findByName("Electronics");
		electronics.getChildren().forEach(category -> log.info(category.toString()));
		assertThat(electronics.getChildren().size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintCategoriesHierarchically() {
		
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(category -> {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				recurseChildren(category,0);
			}
		});
		
	}
	
	@Test
	public void testfindByNameLikeAndAliasLike() {
		List<Category> categories = categoryRepository.findByNameLikeOrAliasLike("%omp%", "%omp%");
		assertThat(categories.size()).isGreaterThan(0);
	}

	private void recurseChildren(Category category, int level) {
		int newLevel = level+1;
		category.getChildren().forEach(child -> {
			for(int i = 0; i < newLevel; i ++) {
				System.out.print("--");
			}
			System.out.println(child.getName());
			recurseChildren(child, newLevel);
		});
	}
	
	@Test
	public void testFindRootCategories() {
		List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(category -> System.out.println(category));
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = categoryRepository.findByName(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	@Test
	public void testFindByAlias() {
		String alias = "elec";
		Category category = categoryRepository.findByAlias(alias);
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
	


}
