package com.rvlt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan("com.rvlt.*")
public class EcommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      // CORS
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000", "https://shop-frend.vercel.app/");
      }

      // Path prefix
      @Override
      public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("blog", HandlerTypePredicate.forAnnotation(RestController.class).and(HandlerTypePredicate.forBasePackage("com.rvlt.blog")));
        configurer.addPathPrefix("ecom", HandlerTypePredicate.forAnnotation(RestController.class).and(HandlerTypePredicate.forBasePackage("com.rvlt.ecommerce")));
      }
    };
  }
}
