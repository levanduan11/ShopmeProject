package com.shopme.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.ProductService;

@Controller
public class ProductController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,Model model) {
		
		return viewCategoryByPage(alias, 1, model);
				
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, 
								@PathVariable("pageNum")int pageNum,
							Model model) {

		Category category=null;
		
		try {
			category = categoryService.getCategory(alias);
			List<Category> categoryPatents = categoryService.getCategoryParents(category);

			Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());

			List<Product>listProduct=pageProduct.getContent();
			
			long start = (pageNum - 1) * ProductService.PAGE + 1;

			long end = pageNum * ProductService.PAGE;

			if (end > pageProduct.getTotalElements()) {
				end = pageProduct.getTotalElements();
			}

		
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", start);
			model.addAttribute("endCount", end);
			model.addAttribute("totalItems", pageProduct.getTotalElements());
			model.addAttribute("totalPages", pageProduct.getTotalPages());
			model.addAttribute("listProducts", listProduct);
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("categoryPatents", categoryPatents);
			model.addAttribute("category", category);
			return "product/products_by_category";
			
		} catch (CategoryNotFoundException e) {
			
			return "error/404";
		}
		
		
		
		
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias")String alias,Model model) {
		
		try {
			Product product=productService.getProduct(alias);
			List<Category> categoryPatents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("product", product);
			model.addAttribute("categoryPatents", categoryPatents);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
		
	}
	@GetMapping("/search")
	public String searchFirstPage(@Param("keyword")String keyword,

						 Model model){



		return searchByPage(keyword,1,model);
	}

	@GetMapping("/search/{pageNum}")
	public String searchByPage(@Param("keyword")String keyword,
						 @PathVariable("pageNum")int pageNum,
						 Model model){

		Page<Product>pageProduct=productService.search(keyword,pageNum);
		List<Product>listResult=pageProduct.getContent();

		long start = (pageNum - 1) * ProductService.PAGE_SERCH + 1;

		long end = pageNum * ProductService.PAGE_SERCH;

		if (end > pageProduct.getTotalElements()) {
			end = pageProduct.getTotalElements();
		}


		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalItems", pageProduct.getTotalElements());
		model.addAttribute("totalPages", pageProduct.getTotalPages());
		model.addAttribute("pageTitle", keyword+" - Search Result");

		model.addAttribute("keyword",keyword);
		model.addAttribute("listResult",listResult);

		return "product/search_result";
	}
}
