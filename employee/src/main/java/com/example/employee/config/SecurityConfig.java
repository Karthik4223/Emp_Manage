package com.example.employee.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.employee.service.impl.CustomEmployeeDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomEmployeeDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomEmployeeDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf().disable()
        .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/home", "/").authenticated()
            .antMatchers("/employeeRequest/**").hasAnyAuthority("RIGHT_EMPLOYEE_REQUEST_CREATE", 
									            		"RIGHT_EMPLOYEE_REQUEST_VIEW", 
									            		"RIGHT_EMPLOYEE_REQUEST_APPROVE_REJECT",
									            		"RIGHT_EMPLOYEE_CREATE_EXCEL")
            
            .antMatchers("/employeeRights/**").hasAnyAuthority("RIGHT_EMPLOYEE_RIGHTS_MAPPING")

            .antMatchers("/employee/**").hasAnyAuthority("RIGHT_EMPLOYEE_CREATE",
            							"RIGHT_EMPLOYEE_VIEW",
							            "RIGHT_EMPLOYEE_EDIT",
							            "RIGHT_EMPLOYEE_CHANGE_STATUS")
            
            .antMatchers("/employeeRights/**").hasAnyAuthority("RIGHT_EMPLOYEE_RIGHTS_MAPPING")
            
            .antMatchers("/rights/**").hasAnyAuthority("RIGHT_RIGHTS_CREATE",
            							"RIGHT_RIGHTS_VIEW",
            							"RIGHT_RIGHTS_CHANGE_STATUS")

            
            
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	  }


}
