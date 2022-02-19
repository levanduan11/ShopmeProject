package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand implements Comparable<Brand> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name",nullable = false,unique = true,length = 45)
	private String name;
	
	@Column(name = "logo",nullable = false,length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories",
			joinColumns = @JoinColumn(name="brand_id"),
			inverseJoinColumns = @JoinColumn(name="category_id")
			
			)
	private Set<Category>categories=new HashSet<>();

	
	
	public Brand() {
		super();
	}

	
	
	public Brand(Integer id) {
		super();
		this.id = id;
	}



	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}


	public Brand(String name, String logo) {
		super();
		this.name = name;
		this.logo = logo;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	@Transient
	public String getPathLogo() {
		if (this.logo==null) {
			return "/images/image-thumbnail.png";
		}
		return "/brand-logo/"+this.id+"/"+this.logo;
	}


	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + "]";
	}



	@Override
	public int compareTo(Brand o) {
		
		return this.getName().compareTo(o.getName());
	}
	
	
	

}
