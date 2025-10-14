package com.example.employee.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		
		String path = request.getRequestURI();

		if (path.startsWith("/auth/login") || path.startsWith("/auth/logout")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    HttpSession session = request.getSession(false);

	    if (session != null) {
	        String redisKey = "user:" + session.getAttribute("empCode");
	        String sessionId = (String) redisTemplate.opsForValue().get(redisKey);

	        if(!session.getId().equals(sessionId)) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Session invalid or expired");
	            return;
	        }
	    } else {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("No session found");
	        return;
	    }

	    filterChain.doFilter(request, response);
	}

}
