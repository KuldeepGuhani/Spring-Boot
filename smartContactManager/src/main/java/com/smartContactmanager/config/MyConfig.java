package com.smartContactmanager.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "")
public class MyConfig  {

	@Autowired
	
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	
		return daoAuthenticationProvider;
	}
	
	//configure methods
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf()
	      .disable().authorizeHttpRequests()
	      .requestMatchers("/admin/**")
	      .hasRole("ADMIN")
	      .requestMatchers("/user/**")
	      .hasAnyRole("USER")
	      .requestMatchers("/**")
	      .permitAll()
	      .and()
	      .formLogin().loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").failureUrl("/signin")
	      .and()
	      ;
//	      
		http.authenticationProvider(authenticationProvider());
//		DefaultSecurityFilterChain build=http.build();
		
		return http.build();
	}
	
}
