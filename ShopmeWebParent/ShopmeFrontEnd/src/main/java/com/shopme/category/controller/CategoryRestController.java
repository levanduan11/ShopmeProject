package com.shopme.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;

@RestController
@RequestMapping("/category")
public class CategoryRestController {
	
	@Autowired 
	private CategoryService service;

	
}
