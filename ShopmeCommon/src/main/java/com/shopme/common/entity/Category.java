package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.bytebuddy.asm.Advice.AllArguments;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 128, nullable = false, unique = true)
	private String name;

	@Column(name = "alias", length = 64, nullable = false, unique = true)
	private String alias;

	@Column(name = "image", length = 128, nullable = false)
	private String image;

	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name ="all_parent_ids",length = 256,nullable = true )
	private String allParentIds;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public Category() {

	}

	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Category(Integer id) {
		super();
		this.id = id;
	}

	public Category(String name) {
		super();
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category(String name, Category parent) {

		this(name);
		this.parent = parent;
	}
	

	public Category(Integer id, String name, String alias) {
	
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public static Category copyIdAndName(Category parent) {
		Category copyCategory = new Category();
		copyCategory.setId(parent.getId());
		copyCategory.setName(parent.getName());

		return copyCategory;
	}

	public static Category copyIdAndName(Integer id,String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);

		return copyCategory;
	}
	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());

		return copyCategory;
	}
	public static Category copyFull(Category category,String name) {
		Category copyCategory = copyFull(category);
		copyCategory.setName(name);

		return copyCategory;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	

	public String getAllParentIds() {
		return allParentIds;
	}

	public void setAllParentIds(String allParentIds) {
		this.allParentIds = allParentIds;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}
	
	@Transient
	public boolean isNochildren() {
		
		return this.children.size()==0;
	}

	@Transient
	public String getImagePath() {
		if (this.id== null) {
			return "/images/image-thumbnail.png";
			
		}

		return "/category-images/" + this.id + "/" + this.image;
	}
	
	

}
