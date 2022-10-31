package com.demo.osiguranje.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(basePackages = {"com.demo.osiguranje", "com.demo.framework"})
public class DemoOsiguranjeApplication {

  
	public static void main(String[] args) {
		SpringApplication.run(DemoOsiguranjeApplication.class, args);
	}

	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			
	    @Value("#{'${cors.allowedOrigins:}'.split(',')}")
	    private String[] corsAllowedOrigins;
			
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				if (this.corsAllowedOrigins != null && this.corsAllowedOrigins.length > 0)
				{
					registry.addMapping("/**").allowedOrigins(this.corsAllowedOrigins).allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
					for (int i = 0; i < this.corsAllowedOrigins.length; i++)
					{
						//registry.addMapping("/**").allowedOrigins(this.corsAllowedOrigins[i]);
					}
				}
			}
		};
	}
}
