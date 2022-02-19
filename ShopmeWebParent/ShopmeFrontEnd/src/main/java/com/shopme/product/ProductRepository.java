package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p from Product p where p.enabled = true "
			+ " and (p.category.id =?1 or p.category.allParentIds like %?2%)"
			+ " order by p.name asc")
	public Page<Product>listByCategory(Integer categoryId,String categoryIdMatch,Pageable pageable);
	
	public Product findByAlias(String alias);

	@Query(value = "select * from products  where enabled=true and " +
			" match (name,short_description,full_description) against (?1)",nativeQuery = true)
	public Page<Product>search(String keyword,Pageable pageable);
	
}
