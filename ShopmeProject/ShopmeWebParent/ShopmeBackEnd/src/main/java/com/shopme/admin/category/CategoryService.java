package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public List<Category> listAll(){
		return listCategoriesinHierArchicalForm();
	}
	
	public List<Category> searchByKeyword(String keyword){
		return categoryRepository.findByNameLikeOrAliasLike("%" + keyword +  "%", "%" + keyword +  "%");
	}
	
	public List<Category> listRootCategories(){
		return categoryRepository.findRootCategories();
	}
	
	public Category getCategoryById(Integer categoryId) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(categoryId).get();
		}
		catch(NoSuchElementException enfe) {
			throw new CategoryNotFoundException(enfe.getMessage(),enfe);
		}
	}
	
	public List<Category> listCategoriesinHierArchicalForm(){
		List<Category> hierarchicalCategories = new ArrayList<>();
		List<Category> categories = categoryRepository.findRootCategories();
		categories.forEach(category -> {
			hierarchicalCategories.add(category);
			recurseChildren(category,0,hierarchicalCategories);
		});
		return hierarchicalCategories;
	}
	
	private void recurseChildren(Category category, int level, List<Category> hierarchicalCategories) {
		int newLevel = level+1;
		category.getChildren().forEach(child -> {
			String prefix = "";
			for(int i = 0; i < newLevel; i ++) {
				prefix += "--";
			}
			Category subCategory = child.copy();
			subCategory.setName(prefix + " " + subCategory.getName());
			hierarchicalCategories.add(subCategory);
			recurseChildren(child, newLevel, hierarchicalCategories);
		});
	}
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

}
