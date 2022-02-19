package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;



@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;
	
//	@GetMapping("")
//	public String firstListPage(@Param("sortDir")String sortDir,Model model) {
//		
//		if (sortDir==null||sortDir.isEmpty()) {
//			sortDir="asc";
//		}
//		
//		List<Category>categories=service.findAll(sortDir);
//		String rever=sortDir.equals("asc")? "desc" :"asc";
//		model.addAttribute("categories", categories);
//		model.addAttribute("reverSortDir", rever);
//		
//		return "categories/categories";
//		
//	}
	
	@GetMapping("")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model, "name", "asc", null);
	}
	
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PathVariable("pageNum")int pageNum,
			Model model,
			@RequestParam("sortField")String sortField,
			@RequestParam("sortDir")String sortDir,
			@Param("keyword")String keyword
			) {
		
		Page<Category>page=service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Category>Categories=page.getContent();
		long startCount=(pageNum-1)*CategoryService.CA_PAGE+1;
		long endCount=pageNum*CategoryService.CA_PAGE;
		if (endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		
		
		String reverSortDir=sortDir.equals("asc") ? "desc":"asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("categories", Categories);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverSortDir", reverSortDir);
		model.addAttribute("keyword", keyword);

		
		
		
		return "categories/categories";
	}
	
	
	
	@GetMapping("/new")
	public String formCategory(Model model) {
		
		Category category=new Category();
		List<Category>listCategories=service.listCategoriesUsedInForm();
		model.addAttribute("category", category);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		
		return "categories/categories_form";
	}
	
	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category")Category category,
								RedirectAttributes dRedirectAttributes,
								@RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category savedCategory=service.save(category);
			String uploadDir="../category-images/"+savedCategory.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			service.save(category);
		}
		
		
		dRedirectAttributes.addFlashAttribute("message", "the Category has been saved successfully");
		return "redirect:/categories";
	}
	
	@GetMapping("/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id,
								Model model,
								RedirectAttributes redirectAttributes) throws CategoryNotFoundException {
		
		try {
			Category category=service.get(id);
			List<Category>listCategories=service.listCategoriesUsedInForm();
			
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			
			return "categories/categories_form";
			
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id,
								RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String categoryDir="../category-images/"+id;
			FileUploadUtil.removeDir(categoryDir);
			
			redirectAttributes.addFlashAttribute("message", "The Category ID " + id + "  has been deleted ");
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/categories";
	}
	
	@GetMapping("{id}/enabled/{status}")
	public String updateCategoryStatus(@PathVariable("id")Integer id,
			@PathVariable("status")boolean enabled,
			RedirectAttributes redirectAttributes) {
		
		service.updateStatus(id, enabled);
		String status=enabled ? "enabled":"disabled";
		String message="the Category id " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		
		return "redirect:/categories";
	}
	
	@GetMapping("/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Category>list=service.listAll();
		CategoryCsvExporter exporter=new CategoryCsvExporter();
		exporter.export(list, response);
	}
}
