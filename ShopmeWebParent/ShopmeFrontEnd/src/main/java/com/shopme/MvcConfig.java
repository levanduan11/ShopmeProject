package com.shopme;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String categoryImageName = "../category-images";
		exposeDirectory(categoryImageName, registry);

		String productImageName="../product-images";
		exposeDirectory(productImageName, registry);
		
		
		
		String brandDirName="../brand-logo";
		exposeDirectory(brandDirName, registry);
		
	}
	
	private void exposeDirectory(String pathPattern,ResourceHandlerRegistry registry) {
		
		Path path=Paths.get(pathPattern);
		String absolute=path.toFile().getAbsolutePath();
		String logicPath=pathPattern.replace("../", "")+"/**";
		registry.addResourceHandler(logicPath).addResourceLocations("file:/"+absolute+"/");
	}

	
}
