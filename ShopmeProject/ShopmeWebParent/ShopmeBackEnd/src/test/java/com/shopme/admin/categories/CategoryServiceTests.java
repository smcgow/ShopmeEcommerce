package com.shopme.admin.categories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
	
	@MockBean
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	@Test
	public void testCheckUniqueInNewModeReturnsDuplicateName(){
		
		String name = "Computers";
		Integer id = null;
		String alias = "comp";
		Category category = Category.builder()
									.name(name)
									.id(id)
									.alias(alias)
									.build();
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnsDuplicateAlias(){
		
		String name = "Abc";
		Integer id = null;
		String alias = "comp";
		Category category = Category.builder()
									.name(name)
									.id(id)
									.alias(alias)
									.build();
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnsDuplicateOk(){
		
		String name = "Abc";
		Integer id = null;
		String alias = "abc";
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnsDuplicateName(){
		
		String name = "Computers";
		Integer id = 123;
		String alias = "comp";
		Category category = Category.builder()
									.name(name)
									.id(id)
									.alias(alias)
									.build();
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		
		String result = categoryService.checkUnique(124, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnsDuplicateAlias(){
		
		String name = "Computers";
		Integer id = 123;
		String alias = "comp";
		Category category = Category.builder()
									.name(name)
									.id(id)
									.alias(alias)
									.build();
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(124, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnsOk(){
		
		String name = "Computers";
		Integer id = 123;
		String alias = "comp";
		Category category = Category.builder()
									.name(name)
									.id(id)
									.alias(alias)
									.build();
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}

}
