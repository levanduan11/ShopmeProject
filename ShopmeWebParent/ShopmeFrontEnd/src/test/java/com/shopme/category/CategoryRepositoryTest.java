package com.shopme.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testListEnabledCate() {
		List<Category>categories=repo.findAllEnabled();
		categories.forEach(x->System.out.println(x.getName()));
	}
	
	@Test
	public void tesFindCategoryByAlias() {
		String alisaString="electronics";
		Category category=repo.findByAliasEnabled(alisaString);
		assertThat(category).isNotNull();
	}
}
