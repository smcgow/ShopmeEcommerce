package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false,length = 64)
	private String alias;
	
	@Column(nullable = false,length = 128,unique = true)
	private String name;
	
	@Column(nullable = false,length = 128)
	private String image;
	
	private boolean enabled;
	
	@OneToOne()
	@JoinColumn(name="parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();

	public Category(String alias, String name, String image, boolean enabled) {
		super();
		this.alias = alias;
		this.name = name;
		this.image = image;
		this.enabled = enabled;
	}
	
}
