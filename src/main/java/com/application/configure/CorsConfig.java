package com.application.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * AJAX请求跨域
 * @author Mr.W
 * @time 2018-08-13
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    static final String ORIGINS[] = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://172.23.160.105:8080")
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .exposedHeaders("**")
                .maxAge(3600);

        System.out.println("wthhhhhhhhhhhhhhhhhh");
    }
}