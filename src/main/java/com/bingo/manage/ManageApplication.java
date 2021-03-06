package com.bingo.manage;

import com.bingo.manage.config.properties.manageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ManageApplication extends WebMvcConfigurerAdapter {

	protected final static Logger logger = LoggerFactory.getLogger(ManageApplication.class);

	@Autowired
	manageProperties manageProperties;

	/**
	 * 增加swagger的支持
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if(manageProperties.getSwaggerOpen()){
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(ManageApplication.class, args);
		logger.info("ManageApplication is success!");
	}
}
