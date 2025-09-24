package com.example.employee.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLogin implements UserDetails{
	private String empCode;
	private String password;
    private List<String> rights;
    
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return rights.stream()
			    .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toList());
	}
	
	@Override
	public String getUsername() {
        return empCode;
	}
	
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
    
    

}
