package com.shopme.admin.category;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategorySeviceTests {

	@MockBean
	private CategoryRepository repo;
	
	@InjectMocks
	private CategoryService service;
	
	@Test
	public void testCheckUniqueDuplicatName() {
		Integer id=null;
		String name="Computers";
		String alias="abc";
		Category byName=new Category(id,name,alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(byName);
		Mockito.when(repo.findByAlias(name)).thenReturn(null);
		
		String result=service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueDuplicatAlias() {
		Integer id=null;
		String name="okem";
		String alias="Computers";
		Category byName=new Category(id,name,alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(byName);
		
		String result=service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueOk() {
		Integer id=null;
		String name="okem";
		String alias="Computers";
		Category byName=new Category(id,name,alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result=service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueEdit() {
		Integer id=1;
		String name="Computers";
		String alias="abc";
		Category byName=new Category(2,name,alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(byName);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result=service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	@Test
	public void testCheckUniqueEditAlias() {
		Integer id=1;
		String name="abc";
		String alias="Computers";
		Category byName=new Category(2,name,alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(byName);
		
		String result=service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
}
