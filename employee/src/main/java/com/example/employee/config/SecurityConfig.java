package com.example.employee.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private SessionAuthorizationFilter sessionAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/employeeRequest/**").hasAnyAuthority(
                        "RIGHT_EMPLOYEE_REQUEST_CREATE",
                        "RIGHT_EMPLOYEE_REQUEST_VIEW",
                        "RIGHT_EMPLOYEE_REQUEST_APPROVE_REJECT",
                        "RIGHT_EMPLOYEE_CREATE_EXCEL")
                
                .antMatchers("/employee/**").hasAnyAuthority(
                        "RIGHT_EMPLOYEE_CREATE",
                        "RIGHT_EMPLOYEE_VIEW",
                        "RIGHT_EMPLOYEE_EDIT",
                        "RIGHT_EMPLOYEE_CHANGE_STATUS")
                
               .antMatchers("/employeeRights/**").hasAnyAuthority("RIGHT_EMPLOYEE_RIGHTS_MAPPING")
              
               .antMatchers("/rights/**").hasAnyAuthority("RIGHT_RIGHTS_CREATE",
              							"RIGHT_RIGHTS_VIEW",
             							"RIGHT_RIGHTS_CHANGE_STATUS")
                
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionFixation().none()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false);
        
        http.addFilterBefore(sessionAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder())
//                .and()
//                .build();
//    }
    
	    @Bean
	    public UserDetailsService userDetailsService() {
	        return username -> {
	            throw new UsernameNotFoundException("Manual login in use, no default user");
	        };
	    }


	  @Bean
	  public PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	  }

	  
	  @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins("http://localhost:3000")
	                        .allowCredentials(true)
	                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	            }
	        };
	    }

}
