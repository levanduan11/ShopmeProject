package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir,String fileName,MultipartFile multipartFile) throws IOException {
		
		Path uploadPath=Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
		try(InputStream inputStream=multipartFile.getInputStream()){
			Path filePath=uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			
			throw new IOException("could not save file :"+fileName,e);
		}
		
	}
	
	public static void cleanDir(String dir) {
		
		Path dirPath=Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(x->{
				if (!Files.isDirectory(x)) {
					try {
						Files.delete(x);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			
		}
		
	}
}
