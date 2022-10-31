package com.demo.osiguranje.beans;

import java.io.PrintWriter;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class GeneralConfigs
{

	
  @Bean("restTemplate")
  @Primary
  public RestTemplate restTemplate() 
  {    
     return new RestTemplate();
  } 
  
  
 /* @Bean("dataSource")
  @Primary
 // @ConfigurationProperties("spring.datasource")
  public HikariDataSource dataSource() 
  {    
  	HikariDataSource hikariDataSource = DataSourceBuilder.create()
              .type(HikariDataSource.class)
             // .driverClassName("org.postgresql.ds.PGSimpleDataSource")
              .url("jdbc:postgresql://localhost:5432/postgres")
              .username("hrvz_lc_workspace")
              .password("hrvz_lc_workspace")
              .build();
  	System.out.println(">>> HikariDataSource kreiranje " + Boolean.toString(hikariDataSource != null));
     return hikariDataSource;
  } */
}
