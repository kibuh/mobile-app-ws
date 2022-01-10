package io.kgpsoft.mobile.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kgpsoft.mobile.app.ws.service.UserService;
import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;
import io.kgpsoft.mobile.app.ws.ui.request.UserDetailsRequestModel;
import io.kgpsoft.mobile.app.ws.ui.response.UserResponse;

@RestController
@RequestMapping("users")
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) {
		
		UserResponse returnUser = new UserResponse();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUser, returnUser);
		
		
		
		return returnUser;
	}

}
