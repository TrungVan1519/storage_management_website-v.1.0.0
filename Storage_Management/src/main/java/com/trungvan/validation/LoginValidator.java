package com.trungvan.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trungvan.entity.User;
import com.trungvan.service.UserService;

@Component
public class LoginValidator implements Validator{

	@Autowired
	private UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return clazz == User.class;
	}

	@Override
	public void validate(Object target, Errors errors) {

		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "msg.required");
		
		if(!StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getPassword())) {
			
			List<User> users = userService.findByProperty("username", user.getUsername());
			if(users != null && !users.isEmpty()) {
				
				if(!users.get(0).getPassword().equals(user.getPassword())) {
					
					errors.rejectValue("password", "msg.wrong.password");
				} 
			} else {
				
				errors.rejectValue("username", "msg.wrong.username");
			}
		}
	}
}
