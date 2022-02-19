package com.shopme.admin.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.product.ProductService;

@RestController
@RequestMapping("/products")
public class ProductRestController {
	
	@Autowired 
	private ProductService service;
	
	@PostMapping("/check_uniqueName")
	public boolean checkUniqueName(@Param("name")String name,@Param("id")Integer id) {
		
		return service.checkNameUnique(id, name);
	}

}
