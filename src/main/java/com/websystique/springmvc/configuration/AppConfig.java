package com.websystique.springmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.websystique.springmvc.converter.RoleToUserProfileConverter;

/**
 * Adding @EnableWebMvc annotation to an @Configuration class imports the Spring MVC configuration from
 *To customize the imported configuration, implement the interface WebMvcConfigurer and override individual methods
 *  addFormatters, addResourceHandlers
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.websystique.springmvc")
public class AppConfig implements WebMvcConfigurer {
	private RoleToUserProfileConverter roleToUserProfileConverter;

	public AppConfig(RoleToUserProfileConverter roleToUserProfileConverter) {
	    this.roleToUserProfileConverter = roleToUserProfileConverter;
    }
    
    /**
     * Configure Converter to be used.
     * In our example, we need a converter to convert string values[Roles] to UserProfiles in registration.jsp
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(roleToUserProfileConverter);
    }

    /**
     * Configure MessageSource to lookup any validation/error message in internationalized property files
     */
    @Bean
	public MessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
}

