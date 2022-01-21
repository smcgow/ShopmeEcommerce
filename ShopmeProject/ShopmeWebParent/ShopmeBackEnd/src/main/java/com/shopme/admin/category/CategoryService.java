package com.shopme.admin.category;

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

}
