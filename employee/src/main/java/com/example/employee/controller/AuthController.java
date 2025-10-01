package com.example.employee.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.customException.EmployeeException;
import com.example.employee.helpers.JwtUtil;
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
    private JwtUtil jwtUtil;
    
    @PostMapping("/login") 
    public ResponseEntity<?> authenticate(@RequestBody EmployeeLogin employeeLogin, HttpSession session) {

        String empCode = employeeLogin.getEmpCode();
        String password = employeeLogin.getPassword();

        try {
            Employee employee = employeeService.getAllEmployeeById(empCode);
            if (employee == null) {
                return ResponseEntity.status(404).body("Employee not found");
            }

            if (employee.getEmployeeStatus() != null && employee.getEmployeeStatus().toString().equals("INACTIVE")) {
                return ResponseEntity.status(403).body("Employee is inactive");
            }
            
            String storedPassword = employee.getEmpPassword();
            boolean passwordMatches = false;

            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
                passwordMatches = passwordEncoder.matches(password, storedPassword);
            } else {
                passwordMatches = password.equals(storedPassword);
                if(passwordMatches) {
                    String hashed = passwordEncoder.encode(storedPassword);
                    employee.setEmpPassword(hashed);
                    employeeService.updateEmployee(employee);
                }
            }

            if (!passwordMatches) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            
            EmployeeRights employeeRights = employeeRightsService.getEmployeeRightsByEmpCode(empCode);
            
            
            List<String> empRightsCodes = Collections.EMPTY_LIST;
            if(employeeRights != null && employeeRights.getRightCode() != null && !employeeRights.getRightCode().isEmpty()) {
            	empRightsCodes = employeeRights.getRightCode();
            }
            
            
            List<String> empRightsNames = Collections.EMPTY_LIST;
            if(employeeRights != null && employeeRights.getRightName() != null && !employeeRights.getRightName().isEmpty()) {
            	empRightsNames = employeeRights.getRightName();
            }
            
            

            String jwtToken = null;
			try {
				jwtToken = jwtUtil.generateToken(employee.getEmpCode(), employee.getName(), empRightsNames);
			} catch (Exception e) {
				e.printStackTrace();
			}

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", jwtToken);
            responseBody.put("rightsNames", empRightsNames);
            responseBody.put("rightsCodes", empRightsCodes);
                        
            return ResponseEntity.ok(responseBody);
            
        } catch (EmployeeException e) {
			log.error(e.getMessage(),e);
            return ResponseEntity.status(500).body("Failed to get employee");
        }

    }
    
    


}
