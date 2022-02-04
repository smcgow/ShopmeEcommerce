package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public static int CATEGORIES_PER_PAGE = 4;
	
	public List<Category> listByPage(String sortDir, int pageNumber, CategoryPageInfo categoryPageInfo){
		return listCategoriesinHierArchicalForm(sortDir,pageNumber,categoryPageInfo);
	}
	
	public List<Category> listAll(String sortDir){
		return listCategoriesinHierArchicalForm(sortDir,0, null);
	}
	
	public List<Category> searchByKeyword(int pageNumber, String keyword,String sortDir, CategoryPageInfo categoryPageInfo){
		PageRequest pageRequest = PageRequest.of(pageNumber-1, CATEGORIES_PER_PAGE, getSort(sortDir));
		Page<Category> pageResponse = categoryRepository.findByNameLikeOrAliasLike("%" + keyword +  "%", "%" + keyword +  "%",pageRequest);
		categoryPageInfo.setTotalElements(pageResponse.getTotalElements());
		categoryPageInfo.setTotalPages(pageResponse.getTotalPages());
		return pageResponse.getContent();
	}
	
	public List<Category> listRootCategories(Sort sort){
		return categoryRepository.findRootCategories(sort);
	}
	
	public Category getCategoryById(Integer categoryId) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(categoryId).get();
		}
		catch(NoSuchElementException enfe) {
			throw new CategoryNotFoundException(enfe.getMessage(),enfe);
		}
	}
	
	public List<Category> listCategoriesinHierArchicalForm(String sortDir, int pagenum, CategoryPageInfo categoryPageInfo){
		
		List<Category> hierarchicalCategories = new ArrayList<>();
		List<Category> categories;
		if(pagenum != 0) {
			Pageable pageable = PageRequest.of(pagenum - 1, CATEGORIES_PER_PAGE, getSort(sortDir));
			Page<Category> pageCategories = categoryRepository.findRootCategories(pageable);
			categoryPageInfo.setTotalElements(pageCategories.getTotalElements());
			categoryPageInfo.setTotalPages(pageCategories.getTotalPages());
			categories = pageCategories.getContent();
		}else {
			categories = categoryRepository.findRootCategories(getSort(sortDir));
		}
		
		categories.forEach(category -> {
			hierarchicalCategories.add(category);
			recurseChildren(category,0,hierarchicalCategories,sortDir);
		});
		return hierarchicalCategories;
	}

	private Sort getSort(String sortDir) {
		Sort sort;
		if(sortDir.equals("asc")) {
			sort = Sort.by("name").ascending();
		}else if(sortDir.equals("desc")) {
			sort = Sort.by("name").descending();
		}else {
			sort = Sort.by("name").ascending();
		}
		return sort;
	}
	
	public void enableDisableCategory(boolean enabled, Integer categoryId) {
		categoryRepository.enableDisableCategory(enabled, categoryId);
	}
	
	private void recurseChildren(Category category, int level, List<Category> hierarchicalCategories, String sortDir) {
		int newLevel = level+1;
		SortedSet<Category> children = sortSubCategories(category.getChildren(),sortDir);
		children.forEach(child -> {
			String prefix = "";
			for(int i = 0; i < newLevel; i ++) {
				prefix += "--";
			}
			Category subCategory = child.copy();
			subCategory.setName(prefix + " " + subCategory.getName());
			hierarchicalCategories.add(subCategory);
			recurseChildren(child, newLevel, hierarchicalCategories, sortDir);
		});
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
		
		SortedSet<Category> sortedCategories = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category category1, Category category2) {
				if(sortDir.equals("desc")) {
					return - (category1.getName().compareTo(category2.getName()));
				}else {
					return category1.getName().compareTo(category2.getName());
				}
			}
			
		});
		sortedCategories.addAll(children);
		return sortedCategories;
	}
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		
		boolean isCreatingNew = id == null || id == 0;
		
		Category categoryByName = categoryRepository.findByName(name);
		Category categoryByAlias = categoryRepository.findByAlias(alias);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicateName";
			}else if(categoryByAlias != null) {
				return "DuplicateAlias";
			}else {
				return "OK";
			}
		}else if(categoryByName != null && categoryByName.getId() != id) {
			return "DuplicateName";
		}else if(categoryByAlias != null && categoryByAlias.getId() != id) {
			return "DuplicateAlias";
		}else {
			return "OK";
		}
	
	}
	
	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = categoryRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new CategoryNotFoundException("The category of ID " + id + " could not be found");
		}
		categoryRepository.deleteById(id);
	}

}
