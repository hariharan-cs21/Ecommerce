package com.springboot.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration 
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrf) -> csrf.disable()) 
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/api/user/signup").permitAll()
					.requestMatchers("/api/seller/register").permitAll()
					.requestMatchers("/api/product/get/**").permitAll()
					.requestMatchers("/api/product/add","/api/product/update").hasAuthority("SELLER")
					.requestMatchers("/api/seller/get-one").hasAuthority("SELLER")
					.requestMatchers("/api/category/add").hasAuthority("EXECUTIVE")
					.requestMatchers("/api/category/**").permitAll()
					.anyRequest().authenticated()
			)
			 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) 
		 .httpBasic(Customizer.withDefaults()); 
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {  
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager getAuthManager(AuthenticationConfiguration auth) 
			throws Exception {
		  return auth.getAuthenticationManager();
	 }
}
