package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

	@Autowired
	public BrandRepository repo;
	
	@Autowired
	private TestEntityManager manager;
	
	@Test
	public void testFindAll() {
		List<Brand>b=repo.findAll();
		b.forEach(x->System.out.println(x));
	}
	@Test
	public void testCreateBrand() {
		Category ca1=manager.find(Category.class, 2);
		Category ca2=manager.find(Category.class, 3);
		Category ca3=manager.find(Category.class, 4);
		Category ca4=manager.find(Category.class, 5);
		
		Brand b1=new Brand("ASUS","image-thumbnail.png");
		b1.addCategory(ca1);
		b1.addCategory(ca2);
		b1.addCategory(ca3);
		b1.addCategory(ca4);
		repo.save(b1);
		assertThat(b1.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByName() {
		String nameString="asus";
		Brand byName=repo.findByName(nameString);
		System.out.println(byName.getName());
		
		
	}
	
	@Test
	public void testCountById() {
		Integer idLong=99;
		Long h=repo.countById(idLong);
		System.out.println(h);
		
		
	}
	
	
}
