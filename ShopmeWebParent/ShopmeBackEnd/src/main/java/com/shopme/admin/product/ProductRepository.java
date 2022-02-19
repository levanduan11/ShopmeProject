package com.shopme.admin.product;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("update  Product p set p.enabled=?2 where id=?1")
	@Modifying
	public void updateSatus(Integer id,boolean status);
	
	@Query("select p from Product p where  p.name like %?1% "
			+ " or p.brand.name like %?1%"
			+ " or p.category.name like %?1%"
			)
	public Page<Product>findAll(String keyword,Pageable pageable);
	
	@Query("select p from Product p where p.category.id =?1 "
			+ " or p.category.allParentIds like %?2%")
	public Page<Product>findAllCategory(Integer categoryId,String categoryIdMatch,
			Pageable pageable);
	
	@Query("select p from Product p where (p.category.id =?1 "
			+ " or p.category.allParentIds like %?2%) and "
			+ " ( p.name like %?3% "
			+ " or p.brand.name like %?3%"
			+ " or p.category.name like %?3%) "
			)
	public Page<Product>searchInCategory(Integer categoryId,String categoryIdMatch,
			String keyword,
			Pageable pageable);
	
	
	public Product findByName(String name);
	
	public Long countById(Integer id);

}
