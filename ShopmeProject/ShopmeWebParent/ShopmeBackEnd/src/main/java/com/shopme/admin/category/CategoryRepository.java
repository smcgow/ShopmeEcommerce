package com.shopme.admin.category;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Category findByName(String name);
	
	Category findByAlias(String alias);
	
	Long countById(Integer id);
	
	Page<Category> findByNameLikeOrAliasLike(String name, String alias, Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.parent IS NULL")
	List<Category> findRootCategories(Sort sort);

	@Query("SELECT c FROM Category c WHERE c.parent IS NULL")
	Page<Category> findRootCategories(Pageable pageable); 
	
	@Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :categoryId")
	@Modifying
	public void enableDisableCategory(boolean enabled, Integer categoryId);
	
	
}
