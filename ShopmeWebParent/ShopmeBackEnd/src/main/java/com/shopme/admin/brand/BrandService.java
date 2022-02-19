package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	public static final int PAGE=7;

	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll() {

		return repo.findAll();
	}

	public Brand save(Brand brand) {

		return repo.save(brand);

	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (Exception e) {
			
			throw new BrandNotFoundException("could not find any Brand with id " + id);
		}
	}
	
	public List<Brand> listAllByExport() {
		
		return repo.findAll(Sort.by("name"));
	}
	
	public Page<Brand> listByPage(int pageNum,String sortFiled,String sortDir,String keyword) {
		
		Sort sort=Sort.by(sortFiled);
		sort=sortDir.equals("asc") ? sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNum-1, PAGE, sort);
		if (keyword !=null) {
			
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long c=repo.countById(id);
		if (c==null || c==0) {
			
			throw new BrandNotFoundException("Not found Brand with id:  "+id);
		}
		repo.deleteById(id);
	}
	
	public String checkNameUnique(Integer id,String name) {
		
		boolean isCreateNew=(id ==null||id==0);
		
		Brand createBrand=repo.findByName(name);
		
		if (isCreateNew&&createBrand!=null) {
			
				return "duplicate Name";
			
		}else if(createBrand !=null&&createBrand.getId()!=id) {
			
			
				return "duplicate Name";
			
		}
		
		return "ok";
		
	}

}
