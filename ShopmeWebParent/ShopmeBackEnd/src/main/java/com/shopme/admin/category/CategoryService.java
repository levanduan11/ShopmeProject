package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	public static final int CA_PAGE=4;
	
	@Autowired
	private CategoryRepository repo;

	public List<Category> findAll(String sortDir) {

		Sort sort=Sort.by("name");
		if (sortDir.equals("asc")) {
			
			sort=sort.ascending();
		}
		else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		return repo.findAll(sort);
	}
	
	public List<Category> findAll() {

		
		return repo.findAll();
	}
	

	
	public Category save(Category category) {
		
		Category parent=category.getParent();
		if (parent!=null) {
			String allParent=parent.getAllParentIds()==null? "-":parent.getAllParentIds();
			allParent+=String.valueOf(parent.getId())+"-";
			category.setAllParentIds(allParent);
		}
		return repo.save(category);
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("could not find any Category with id " + id);
		}
	}

	public void delete(Integer id) throws CategoryNotFoundException {
		
		String mess="Could not find any Category with ID " + id;
		
		try {
			
			Category deleteCategory= repo.findById(id).get();
			
			Set<Category>chil=deleteCategory.getChildren();
			
			if (chil!=null) {
				mess="could not delete category because have category childrent";
			}
			repo.deleteById(id);
		} catch (Exception e) {
			throw new CategoryNotFoundException(mess);
		}
		
		
	}
	
	public String checkUnique(Integer id,String name,String alias) {
		
		boolean isCreating=(id == null ||id==0);
		
		Category byName=repo.findByName(name);
		
		if (isCreating) {
			if (byName != null) {
				return "DuplicateName";
			}else {
				Category byAlias=repo.findByAlias(alias);
				if (byAlias != null) {
					
					return "DuplicateAlias";
					
				}
			}
		}else {
			if (byName != null&&byName.getId()!=id) {
				return "DuplicateName";
			}
			Category byAlias=repo.findByAlias(alias);
			if (byAlias != null&&byAlias.getId()!=id) {
				
				return "DuplicateAlias";
				
			}
			
		}
		
		return "OK";
	}
	
	public List<Category> listAll() {
		
		return repo.findAll(Sort.by("name").ascending());
	}
	
	
	public Page<Category> listByPage(int pageNum,String sortFiled,String sortDir,String keyword) {
		
		Sort sort=Sort.by(sortFiled);
		
		sort=sortDir.equals("asc") ? sort.ascending():sort.descending();
		
		Pageable pageable=PageRequest.of(pageNum-1, CA_PAGE, sort);
		
		if (keyword!=null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category>children) {
		
		SortedSet<Category>sortedChildren=new TreeSet<>((a,b)->a.getName().compareTo(b.getName()));
		
		sortedChildren.addAll(children);
		
		return sortedChildren;
		
	}
	
	
	public void updateStatus(Integer id,boolean enabled) {
		
		 repo.updateCategoryStatus(id, enabled);
		
	}
	
	
	public List<Category> listCategoriesUsedInForm() {

		List<Category> categoriesInForm = new ArrayList<>();
		List<Category> dbCategories = repo.findAll(Sort.by("name").ascending());
		for (Category category : dbCategories) {

			if (category.getParent() == null) {
				categoriesInForm.add(category.copyIdAndName(category));
			}
			if (category.getParent() != null) {
				continue;
			}

			helper(category, categoriesInForm, "+");

		}

		return categoriesInForm;
	}

	public void helper(Category parent, List<Category> list, String ok) {
		if (parent.getChildren() == null) {
			return;
		}

		if (parent.getChildren() != null) {
			for (Category category : parent.getChildren()) {

				String name = ok + category.getName();

				list.add(category.copyIdAndName(category.getId(), name));
				helper(category, list, "+" + ok);
			}
		}

	}
	private List<Category> listHierachicalCategories() {
		
		List<Category> categoriesInForm = new ArrayList<>();
		List<Category> dbCategories = repo.findAll();
		for (Category category : dbCategories) {

			if (category.getParent() == null) {
				categoriesInForm.add(category);
			}
			if (category.getParent() != null) {
				continue;
			}

			helper1(category, categoriesInForm, "+");

		}

		return categoriesInForm;
		
	}
	public void helper1(Category parent, List<Category> list, String ok) {
		if (parent.getChildren() == null) {
			return;
		}

		if (parent.getChildren() != null) {
			for (Category category : parent.getChildren()) {

				String name = ok + category.getName();
				

				list.add(Category.copyFull(category, name));
				helper(category, list, "+" + ok);
			}
		}

	}

}

