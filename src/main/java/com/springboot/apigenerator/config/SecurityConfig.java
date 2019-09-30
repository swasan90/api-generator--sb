/**
 * 
 */
package com.springboot.apigenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.springboot.apigenerator.jwt.JWTAuthenticationFilter;
import com.springboot.apigenerator.jwt.JWTAuthorizationFilter;


/**
 * @author swathy
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.allowed.origin}")
	private String allowedOrigin;
	
	/**
	 * Function to define the cors filter setting.
	 * 
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);		 
		config.addAllowedOrigin(allowedOrigin);
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);

	}
	
	/**
	 * Function to implement the rules to allow the http url's.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()				 
					.anyRequest().authenticated()
					.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.addFilter(new JWTAuthenticationFilter())
					.addFilter(new JWTAuthorizationFilter(authenticationManager()));
					 

		http.headers().frameOptions().disable();

	}

}
