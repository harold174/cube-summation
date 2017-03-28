package com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.storage.StorageProperties;
import com.storage.StorageService;

/**
 * Main initializer spring class
 *  
 * @author harold.murcia
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CubeSummationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubeSummationApplication.class, args);
	}

	/**
	 * Spring storage service initializer
	 * 
	 * @param storageService
	 */
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
	
	
}