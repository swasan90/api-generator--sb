package com.springboot.apigenerator.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res)  {	 
		res.addHeader("access-control-expose-headers", "Authorization");
	}

}
