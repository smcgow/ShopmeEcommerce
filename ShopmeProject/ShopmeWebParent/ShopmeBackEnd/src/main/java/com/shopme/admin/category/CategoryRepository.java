package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Category findByName(String name);
	
	List<Category> findByNameLikeOrAliasLike(String name, String alias);
	
	@Query("SELECT c FROM Category c WHERE c.parent IS NULL")
	List<Category> findRootCategories();

}
