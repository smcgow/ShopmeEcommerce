package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public List<Category> listAll(){
		return categoryRepository.findAll();
	}
	
	public List<Category> searchByKeyword(String keyword){
		return categoryRepository.findByNameLikeOrAliasLike("%" + keyword +  "%", "%" + keyword +  "%");
	}
	
	public List<Category> listCategoriesinHierArchicalForm(){
		List<Category> hierarchicalCategories = new ArrayList<>();
		List<Category> categories = categoryRepository.findAll();
		categories.forEach(category -> {
			if(category.getParent() == null) {
				hierarchicalCategories.add(category);
				recurseChildren(category,0,hierarchicalCategories);
			}
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
			child.setName(prefix + " " + child.getName());
			hierarchicalCategories.add(child);
			recurseChildren(child, newLevel, hierarchicalCategories);
		});
	}

}
