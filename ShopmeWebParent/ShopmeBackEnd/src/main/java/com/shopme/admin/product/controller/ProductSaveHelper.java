package com.shopme.admin.product.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

public class ProductSaveHelper {

	public static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImage="../product-images/"+product.getId()+"/extras";
		Path dirPath=Paths.get(extraImage);
		
		try {
			Files.list(dirPath).forEach(
					file->{
						String fileName=file.toFile().getName();
						if (!product.containsImageName(fileName)) {
							try {
								Files.delete(file);
								System.out.println("deleted extra image"+fileName);
								
								
							} catch (IOException e) {
							
								System.out.println("could not delete"+fileName);
							}
						}
					}
					);
		} catch (IOException e) {
			
		
			System.out.println("could not delete extra image "+dirPath);
		}
		
	}

	public static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
		
		if (imageIds==null||imageIds.length==0) {
			return;
		}
		Set<ProductImage>productImages=new HashSet<>();
		for (int i = 0; i < imageIds.length; i++) {
			Integer id=Integer.parseInt(imageIds[i]);
			String name=imageNames[i];
			productImages.add(new ProductImage(id, name, product));
		}
		product.setProductImages(productImages);
		
	}

	public static void setPoductDetails(String[] detailIds,String[] detailNames, String[] detailValues, Product product) {
	
		if (detailNames==null||detailNames.length==0) {
			return;
		}
		for (int i = 0; i < detailNames.length; i++) {
			String name = detailNames[i];
			String value=detailValues[i];
			Integer id=Integer.parseInt(detailIds[i]);
			if (id !=0 ) {
				
				product.addDetail(id,name, value);
				
			}else if (!name.isEmpty()&&!value.isEmpty()) {
				product.addDetail(name, value);
			}
			
			
			
		}
		
	}

	public static void saveUploadImages(MultipartFile main, MultipartFile[] extra, Product savedProduct) throws IOException {
		
		if (!main.isEmpty()) {
			String fileName=StringUtils.cleanPath(main.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, main);
			
		}
		
		if (extra.length>0) {
			String upLoadDir="../product-images/"+savedProduct.getId()+"/extras";
			for (MultipartFile multipartFile : extra) {
				if (!multipartFile.isEmpty()) {
					String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
					FileUploadUtil.saveFile(upLoadDir, fileName, multipartFile);
				}
				
			}
		}
		
	}

	public static void setNewExtraImageName(MultipartFile[] extra, Product product) {
		
		if (extra.length>0) {
			
			for (MultipartFile multipartFile : extra) {
				if (!multipartFile.isEmpty()) {
					String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					
					
					
				}
			}
		}
		
	}

	public static void setMainImageName(MultipartFile main, Product product) {

		if (!main.isEmpty()) {
			String fileName = StringUtils.cleanPath(main.getOriginalFilename());
			product.setMainImage(fileName);
			
		}

	}

}
