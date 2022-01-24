package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		String directoryName = "user-photos";
		Path userPhotoPath = Paths.get(directoryName);
		String userPhotoFilePath = userPhotoPath.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + directoryName + "/**")
				.addResourceLocations("file:/" + userPhotoFilePath + "/");
		
		String categoryImageDirectoryName = "../category-images";
		Path categoryImagePath = Paths.get(categoryImageDirectoryName);
		String categoryImageFilePath = categoryImagePath.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/category-images/**")
		.addResourceLocations("file:/" + categoryImageFilePath + "/");
	}

	
		
}
