package com.shopme.admin.brand.controller;

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
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.export.BrandCsvExporter;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
@RequestMapping("/brands")
public class BrandController {
	
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("")
	public String listAll(Model model) {
		
		
		return listPage(1, "name", "asc", null, model);
	}
	
	@GetMapping("/page/{pageNum}")
	public String listPage(@PathVariable("pageNum")int pageNum,
							@RequestParam("sortField")String sortFiled,
							@RequestParam("sortDir")String sortDir,
							@Param("keyword")String keyword,
							Model model) {
		
		Page<Brand>page=service.listByPage(pageNum, sortFiled, sortDir, keyword);
		List<Brand>listBrands=page.getContent();
		
		long startCount=(pageNum-1)*BrandService.PAGE+1;
		long endCount=pageNum*BrandService.PAGE;
		
		if (endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		
		String reverSortDir=sortDir.equals("asc") ? "desc":"asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortField", sortFiled);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverSortDir", reverSortDir);
		model.addAttribute("keyword", keyword);
		return "brands/brands";
		
	}
	
	@GetMapping("/new")
	public String showForm(Model model) {
		List<Category>categories=categoryService.listCategoriesUsedInForm();
		Brand brand=new Brand();
		model.addAttribute("brand", brand);
		model.addAttribute("listCategories", categories);
		model.addAttribute("pageTitle", "Create Brand");
		
		return "brands/brands_form";
	}
	
	@PostMapping("/save")
	public String save(
			@ModelAttribute("brand")Brand brand,
			RedirectAttributes redirectAttributes,
			@RequestParam("fileImage")MultipartFile multipartFile
			) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand saved=service.save(brand);
			String uploadDir="../brand-logo/"+saved.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
			
		}else {
			service.save(brand);
		}
		redirectAttributes.addFlashAttribute("message", "the brand has been saved successfully");
		
		return "redirect:/brands";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable("id")Integer id,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		try {
			Brand brand=service.get(id);
			List<Category>categories=categoryService.listCategoriesUsedInForm();
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", categories);
			
			return "brands/brands_form";
			
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
		
		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id")Integer id,
						RedirectAttributes redirectAttributes) {
		
		try {
			service.delete(id);
			String categoryDir="../brand-logo/"+id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "brand with id: "+id+"  have been deleted succssyfully");
			
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/brands";
	}
	
	@GetMapping("/export/csv")
	public void exporCsv(HttpServletResponse response) throws IOException {
		
		List<Brand>list=service.listAllByExport();
		BrandCsvExporter cvvBrandCsvExporter=new BrandCsvExporter();
		cvvBrandCsvExporter.export(response, list);
		
	}

}
