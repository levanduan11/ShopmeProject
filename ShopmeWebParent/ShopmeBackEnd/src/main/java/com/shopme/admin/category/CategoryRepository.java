package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Category;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.parent.id is Null")
	public List<Category>findRootCategories(Sort sort);
	
	@Query("update Category c set c.enabled =?2 where c.id =?1")
	@Modifying
	public void updateCategoryStatus(Integer id,boolean satus);
	
	@Query("SELECT u FROM Category u WHERE CONCAT(u.id,' ',u.name,' ',u.alias) LIKE %?1%")
	public Page<Category>findAll(String keyword,Pageable pageable);
	
	public Category findByName(String name);
	
	public Category findByAlias(String name);
}
