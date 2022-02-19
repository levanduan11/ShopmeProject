package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Brand;

@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand, Integer> {
	
	@Query("select b from Brand b where lower(b.name)=?1")
	public Brand findByName(String name);
	
	@Query("select b from Brand b where concat(b.id,' ',lower(b.name)) like %?1%")
	Page<Brand>findAll(String keyword,Pageable pageable);
	
	public Long countById(Integer id);
	
	@Query("select new Brand(b.id,b.name) from Brand b order by b.name asc")
	public List<Brand>findAll();

}
