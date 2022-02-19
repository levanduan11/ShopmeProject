package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.intThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	

	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category=new Category("Electronics");
		Category saved=repo.save(category);
		
		assertThat(saved.getId()).isGreaterThan(0);
		
		
	}
	
//	@Test
//	public void testCreateSubCategory() {
//		Category parent=new Category(9);
//		Category cameras=new Category("Gaming laptop1",parent);
//		Category cameras1=new Category("Gaming laptop2",parent);
//		Category cameras2=new Category("Gaming laptop3",parent);
//		Category cameras3=new Category("Gaming laptop4",parent);
//		Category cameras4=new Category("Gaming laptop5",parent);
//		List<Category>list=Arrays.asList(cameras,cameras1,cameras2,cameras3,cameras4);
//		repo.saveAll(list);
//		
//	}
	
//	@Test
//	public void testGetCategory() {
//		Category parent=repo.findById(2).get();
//		Set<Category>subCategories=parent.getChildren();
//		subCategories.forEach(x->System.out.println(x.getName()));
//		System.out.println(parent.getName());
//	
//		
//	}
	@Test
	public void testPrintHieraCategory() {
		
		List<Category>categories=repo.findAll();
		
		for (Category category : categories) {
			
			if (category.getParent()==null) {
				System.out.println("-"+category.getName());
			}
			if (category.getParent()!=null) {
				continue;
			}
		
				helper(category, " + ");
			
		}
		
	
		
	}
	public void helper(Category parent,String ok) {
		if (parent.getChildren()==null) {
			return;
		}
		
		if (parent.getChildren()!=null) {
			for (Category category : parent.getChildren()) {
				
				System.out.println(ok+category.getName());
				
				helper(category,"    "+ok);
			}
		}
		
	}
	
	@Test
	public void testListRoot() {
		List<Category>categories=repo.findRootCategories(Sort.by("name").ascending());
		categories.forEach(x->System.out.println(x.getName()));
	
	}
	
	@Test
	public void testFindByName() {
		String name="Computers";
		Category category=repo.findByName(name);
		System.out.println(category.getAlias());
		assertThat(category.getName()).isEqualTo(name);
	
	}
	
	@Test
	public void testFindByAlias() {
		String name="Computers";
		Category category=repo.findByAlias(name);
		System.out.println(category.getAlias());
		assertThat(category.getName()).isEqualTo(name);
	
	}
	
	@Test
	public void testListFirst() {
		int pageNum=0;
		int pageSize=4;
		
		Pageable pageable=PageRequest.of(pageNum, pageSize);
		Page<Category>page=repo.findAll(pageable);
		List<Category>list=page.getContent();
		list.forEach(x->System.out.println(x.getAlias()));
		
		assertThat(list.size()).isEqualTo(pageSize);
	
	}
	@Test
	public void testSearchCategory() {
		int pageNum=0;
		int pageSize=4;
		String keyWord="Com";
		
		Pageable pageable=PageRequest.of(pageNum, pageSize);
		Page<Category>page=repo.findAll(keyWord,pageable);
		List<Category>list=page.getContent();
		list.forEach(x->System.out.println(x.getAlias()));
		
		
	
	}
}
