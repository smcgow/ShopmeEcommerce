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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	@Builder.Default
	private Set<Category> children = new HashSet<>();

	@Override
	public String toString() {
		return "Category [id=" + id + ", alias=" + alias + ", name=" + name + ", image=" + image + ", enabled="
				+ enabled + "]";
	}
	
	public String getImagePath() {
		if(this.getImage() == null || this.getImage().isBlank()) {
			return "/images/image-thumbnail.png";
		}
		return "/category-images/" + this.getId() + "/" + this.getImage();
	}

	public Category copy() {
		return Category.builder()
				.alias(this.getAlias())
				.enabled(this.isEnabled())
				.id(this.getId())
				.image(this.getImage())
				.name(this.getName())
				.build();
	}
	
	
}
