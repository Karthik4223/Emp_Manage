package com.example.employee.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeLogin;
import com.example.employee.model.EmployeeRights;
import com.example.employee.service.EmployeeRightsService;
import com.example.employee.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private EmployeeRightsService employeeRightsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    

    
    @PostMapping("/login") 
    public ResponseEntity<?> authenticate(@RequestBody EmployeeLogin employeeLogin, HttpServletRequest request) {

        String empCode = employeeLogin.getEmpCode();
        String password = employeeLogin.getPassword();

        try {
            Employee employee = employeeService.getEmployeeById(empCode);
            if (employee == null) {
                return ResponseEntity.status(404).body("Employee not found");
            }

            if (employee.getEmployeeStatus() != null && "INACTIVE".equals(employee.getEmployeeStatus().toString())) {
                return ResponseEntity.status(403).body("Employee is inactive");
            }
            
            String storedPassword = employee.getEmpPassword();
            boolean passwordMatches = false;

            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
                passwordMatches = passwordEncoder.matches(password, storedPassword);
            } else {
                passwordMatches = password.equals(storedPassword);
            }

            if (!passwordMatches) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            
            EmployeeRights employeeRights = employeeRightsService.getEmployeeRightsByEmpCode(empCode);
            
            List<String> empRightsNames = Collections.EMPTY_LIST;
            if(employeeRights != null && employeeRights.getRightName() != null && !employeeRights.getRightName().isEmpty()) {
            	empRightsNames = employeeRights.getRightName();
            }
            

            HttpSession session = request.getSession(true);

            session.setAttribute("empCode", employee.getEmpCode());
            session.setAttribute("empName", employee.getName());
            session.setAttribute("rightsNames", empRightsNames);
            session.setMaxInactiveInterval(30 * 60);
            
            List<GrantedAuthority> authorities = empRightsNames.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            EmployeeLogin userDetails = new EmployeeLogin(empCode, null, empRightsNames);
            
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
            
            String redisUserKey = "user:" + empCode;
            redisTemplate.opsForValue().set(redisUserKey, session.getId(),30,TimeUnit.MINUTES);

            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("empCode", employee.getEmpCode());
            responseBody.put("empName", employee.getName());
            responseBody.put("rightsNames", empRightsNames);
                       
            return ResponseEntity.ok(responseBody);
            
        } catch (EmployeeException e) {
			log.error(e.getMessage(),e);
            return ResponseEntity.status(500).body("Failed to get employee");
        }

    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
        	String redisKey = "user:" + session.getAttribute("empCode");
	        String sessionId = (String) redisTemplate.opsForValue().get(redisKey);

	        if(session.getId().equals(sessionId)) {
	        	String empCode = (String) session.getAttribute("empCode");
	        	redisTemplate.delete("user:" + empCode);
	        }
             
             session.invalidate();
        }
        
        
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");          
        cookie.setHttpOnly(true);     
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        
        return ResponseEntity.ok("Logged out successfully");
    }
    
    
    @GetMapping("/sessionInfo")
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(401).body("No active session");
        }

        String empCode = (String) session.getAttribute("empCode");
        String empName = (String) session.getAttribute("empName");
        List<String> rightsNames = (List<String>) session.getAttribute("rightsNames");

        Map<String, Object> response = new HashMap<>();
        response.put("empCode", empCode);
        response.put("empName", empName);
        response.put("rightsNames", rightsNames);

        return ResponseEntity.ok(response);
    }



}
