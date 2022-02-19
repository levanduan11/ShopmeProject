package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {

	public static final int PAGE_SIZE = 9;
	@Autowired
	private ProductRepository repo;

	public List<Product> listAll() {

		return repo.findAll(Sort.by("name").ascending());
	}

	public Page<Product> listByPage(int pageNum, String sortField, String sorDir,
			String keyword,Integer categoryId) {

		Sort sort = Sort.by(sortField);
		sort = sorDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);
		if (keyword != null&&!keyword.isEmpty() ) {
			
			if (categoryId !=null && categoryId > 0) {
				
				String categoryIdMath ="-"+String.valueOf(categoryId)+"-";
				
				return repo.searchInCategory(categoryId, categoryIdMath, keyword, pageable);
			}
			
			return repo.findAll(keyword, pageable);
		}
		
		if (categoryId !=null && categoryId > 0) {
			
			String categoryIdMath ="-"+String.valueOf(categoryId)+"-";
			
			return repo.findAllCategory(categoryId, categoryIdMath, pageable);
		}

		return repo.findAll(pageable);

	}

	public Product save(Product product) {
		if (product.getId() == null || product.getId() == 0) {
			product.setCreatedTime(new Date());
		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {

			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return repo.save(product);
	}
	
	public void saveProductPrice(Product productInForm) {
		Product productInDb=repo.findById(productInForm.getId()).get();
		productInDb.setCost(productInForm.getCost());
		productInDb.setPrice(productInForm.getPrice());
		productInDb.setDiscountPercent(productInForm.getDiscountPercent());
		
		repo.save(productInDb);
	}

	public Product get(Integer id) throws ProductNotFoundException {

		try {

			return repo.findById(id).get();

		} catch (Exception e) {
			throw new ProductNotFoundException("not found product with id" + id);
		}

	}

	public boolean checkNameUnique(Integer id, String name) {
		Product product = repo.findByName(name);
		if ((id == null || id == 0) && product != null) {
			return false;
		} else if (product != null && id != product.getId()) {

			return false;
		}

		return true;
	}

	public void updateSatus(Integer id, boolean status) {
		repo.updateSatus(id, status);
	}

	public void delete(Integer id) throws ProductNotFoundException {

		Long countById = repo.countById(id);
		if (countById == 0 || countById == null) {
			throw new ProductNotFoundException("not found Product with id: " + id);
		}
		repo.deleteById(id);
	}

}
