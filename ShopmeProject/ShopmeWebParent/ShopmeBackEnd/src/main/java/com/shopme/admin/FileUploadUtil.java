package com.shopme.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadUtil {
	
	public static void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException {
		
		Path uploadPath = Paths.get(uploadDirectory);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try {
			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException exc) {
			throw new IOException("Could not copy file " + fileName, exc);
		}
	}
	
	public static void cleanDirectory(String directory) {
		Path directoryPath = Paths.get(directory);
		
		try {
			Files.list(directoryPath).forEach(filePath -> { 
				File file = filePath.toFile();
				if(!file.isDirectory()) {
					file.delete();
				}
			});
		} catch (IOException e) {
			log.error("Could not list files in directory");
		}
	}
	
	public static void removeDirectory(String directory) {
		cleanDirectory(directory);
		
		try {
			Files.delete(Paths.get(directory));
		} catch (IOException e) {
			log.error("Could not remove directory " + directory);
		}
	}

}
