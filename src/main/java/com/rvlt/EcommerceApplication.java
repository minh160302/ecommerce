package com.rvlt;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan("com.rvlt.*")
@ComponentScan("com.rvlt.*")
@EnableJpaRepositories("com.rvlt.*.repository")
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
        registry.addMapping("/**").allowedOrigins("http://localhost:3000", "https://blueprint-ecommerce.vercel.app/",
                "https://blueprint-ecommerce-git-test-sept15-tuanminh160302s-projects.vercel.app/");
      }

      // Path prefix
      @Override
      public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("bl", HandlerTypePredicate.forAnnotation(RestController.class).and(HandlerTypePredicate.forBasePackage("com.rvlt.blog")));
        configurer.addPathPrefix("ec", HandlerTypePredicate.forAnnotation(RestController.class).and(HandlerTypePredicate.forBasePackage("com.rvlt.ecommerce")));
      }
    };
  }
}
