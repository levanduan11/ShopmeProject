package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private TestEntityManager manager;

	@Test
	public void testCreateProduct() {

		for (int i = 0; i < 9; i++) {
			Brand brand = manager.find(Brand.class, 2);
			Category category = manager.find(Category.class, 5);

			Product product = new Product();
			product.setName("iphoneZ" + i);
			product.setAlias("iphoneZ" + i);

			product.setShortDescription("a good smartphone ");
			product.setFullDescription("this is a very good smartphone full description");

			product.setBrand(brand);
			product.setCategory(category);

			product.setPrice(123);
			product.setCost(400);
			product.setEnabled(true);
			product.setInStock(true);

			product.setCreatedTime(new Date());
			product.setUpdatedTime(new Date());

			Product saved = repo.save(product);
		}

	}

	@Test
	public void testListAll() {
		List<Product> products = repo.findAll();
		products.forEach(x -> System.out.println(x.getName()));

	}

	@Test
	public void testGetProduct() {
		Integer id = 1;
		Product product = repo.findById(id).get();
		System.out.println(product.getName());

	}

	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product edit = repo.findById(id).get();
		edit.setName("java springboot");
		assertThat(edit.getName()).isEqualTo("java springboot");

	}

	@Test
	public void testFindByName() {
		String name = "iphonez8";
		Product product = repo.findByName(name);
		System.out.println(product.getName());
		assertThat(product).isNotNull();

	}

	@Test
	public void testDeleteProduct() {
		Integer id = 2;
		repo.deleteById(id);
		Optional<Product> pOptional = repo.findById(id);
		assertThat(!pOptional.isPresent());

	}

	@Test
	public void testUpdateStatus() {
		Integer id = 3;

		repo.updateSatus(id, false);
		Product product = repo.findById(id).get();
		System.out.println(product.isEnabled());
		assertThat(product.isEnabled());
	}

	@Test
	public void testCountById() {
		Integer id = 3777;

		long d = repo.countById(id);

		assertThat(d).isEqualTo(0);
	}

	@Test
	public void testSaveProductWithImage() {

		Integer id = 1;
		Product product = repo.findById(id).get();
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.png");
		product.addExtraImage("extra image 2.png");
		product.addExtraImage("extra image 3.png");
		Product saved = repo.save(product);
		assertThat(saved.getProductImages().size()).isEqualTo(3);
	}

	@Test
	public void testSaveProductWithDetails() {

		Integer id = 1;
		Product product = repo.findById(id).get();
		product.addDetail("java inaction", "java 8");
		product.addDetail("java inaction", "java 9");
		product.addDetail("java inaction", "java 17");
		repo.save(product);
		assertThat(product.getProductDetails().size()).isEqualTo(3);
	}

	@Test
	public void testFindByKeyword() {

		List<Product>list=repo.findAll(Sort.by("brand"));
		list.forEach(z->System.out.println(z.getBrand().getName()));
		
		
	
	}
}
