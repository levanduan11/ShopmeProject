package com.shopme.admin.product.controller;

import java.io.IOException;

import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;

import com.shopme.admin.product.ProductService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;


@Controller
@RequestMapping("/products")
public class ProductController {
	
	

	@Autowired
	private ProductService service;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BrandService brandService;

	@GetMapping("")
	public String listFirstPage(Model model) {


		return listByPage(1, "name", "asc",null, 0,model);
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum")int pageNum,
								@RequestParam("sortField")String sortField,
								@RequestParam("sortDir")String sortDir,
								@Param("keyword")String keyword,
								@Param("categoryId")Integer categoryId,
								Model model
							) {
		
		
		Page<Product>page=service.listByPage(pageNum, sortField, sortDir, keyword,categoryId);
		List<Product> listProducts =page.getContent();
		List<Category> categories = categoryService.listCategoriesUsedInForm();
		
		long start=(pageNum-1)*ProductService.PAGE_SIZE+1;
		
		long end=pageNum*ProductService.PAGE_SIZE;
		
		if (end>page.getTotalElements()) {
			end=page.getTotalElements();
		}
		
		String reverSortDir=sortDir.equals("asc")? "desc":"asc";
		
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}
		model.addAttribute("listCategories", categories);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverSortDir", reverSortDir);
		
		return "products/products";
	}

	@GetMapping("/new")
	public String newProduct(Model model) {

		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		Integer  numberOfExistingExtraImages=product.getProductImages().size();
		product.setEnabled(true);
		product.setInStock(true);
		

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
		return "products/product_form";
	}

	@PostMapping("/save")
	public String saveProduct(@ModelAttribute("product") Product product,
			@RequestParam(value = "fileImage",required = false) MultipartFile main,
			@RequestParam(value ="extraImage",required = false) MultipartFile[] extra,
			@RequestParam(name = "detailNames",required = false)String[]detailNames,
			@RequestParam(name = "detailValues",required = false)String[]detailValues,
			@RequestParam(name = "detailsId",required = false)String[]detailsId,
			@RequestParam(name = "imageIds",required = false)String[]imageIds,
			@RequestParam(name = "imageNames",required = false)String[]imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			RedirectAttributes redirectAttributes)
			throws IOException {
		
		

		if (loggedUser.hasRole("Salesperson")) {
			service.saveProductPrice(product);
			redirectAttributes.addFlashAttribute("message", "the product has been saved successfully");

			return "redirect:/products";
		}
		
		ProductSaveHelper.setMainImageName(main, product);
		
		ProductSaveHelper.setExistingExtraImageNames(imageIds,imageNames,product);
		
		ProductSaveHelper.setNewExtraImageName(extra,product);
		
		
		ProductSaveHelper.setPoductDetails(detailsId,detailNames,detailValues,product);

		Product savedProduct = service.save(product);
		
		ProductSaveHelper.saveUploadImages(main,extra,savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		

		redirectAttributes.addFlashAttribute("message", "the product has been saved successfully");

		return "redirect:/products";
	}

	
	@GetMapping("{id}/enabled/{status}")
	public String updateStaus(@PathVariable("id") int id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes

	) {
		service.updateSatus(id, status);
		String statu = status ? "enabled" : "disabled";
		String message = "the product id " + id + " has been " + statu;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}
	
	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable("id")Integer id,
								RedirectAttributes redirectAttributes,
			Model model) {

		try {
			Product product = service.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getProductImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product ID: " + id);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}

	}
	
		@GetMapping("/detail/{id}")
	public String viewProductDetail(@PathVariable("id")Integer id,
								RedirectAttributes redirectAttributes,
			Model model) {

		try {

			Product product = service.get(id);
			
			model.addAttribute("product", product);
			
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String productDir = "../product-images/" + id;
			String productExtraDir = "../product-images/" + id+"/extras";
			
			
			FileUploadUtil.removeDir(productExtraDir);
			FileUploadUtil.removeDir(productDir);

			redirectAttributes.addFlashAttribute("message", "the Product with id: " + id + " have been deleted");
		} catch (ProductNotFoundException e) {

			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/products";
	}
}
