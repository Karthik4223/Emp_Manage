package com.example.employee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeContoller {
	
	@RequestMapping(value= {"","/","home"})
	public String home() {
		return "Welcome";
	}

}
