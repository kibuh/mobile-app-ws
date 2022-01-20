package io.kgpsoft.mobile.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kgpsoft.mobile.app.ws.exceptions.UserServiceException;
import io.kgpsoft.mobile.app.ws.service.UserService;
import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;
import io.kgpsoft.mobile.app.ws.ui.model.request.UserDetailsRequestModel;
import io.kgpsoft.mobile.app.ws.ui.model.response.ErrorMessages;
import io.kgpsoft.mobile.app.ws.ui.model.response.OperationStatusModel;
import io.kgpsoft.mobile.app.ws.ui.model.response.RequestOperationStatus;
import io.kgpsoft.mobile.app.ws.ui.model.response.UserResponse;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserResponse returnUser = new UserResponse();

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.createUser(userDto);

		BeanUtils.copyProperties(createdUser, returnUser);

		return returnUser;
	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponse getUser(@PathVariable String id) {

		UserResponse returnUser = new UserResponse();
		UserDto userDto = userService.getUserByUserId(id);

		BeanUtils.copyProperties(userDto, returnUser);

		return returnUser;
	}
	
	
	@PutMapping(path = "/{id}",consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public  UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		UserResponse returnUser = new UserResponse();
		
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);

		BeanUtils.copyProperties(updatedUser, returnUser);
		
		
		return returnUser;
		
	}
	
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel operationStatus = new OperationStatusModel();
		
		userService.deleteUser(id);
		
		operationStatus.setOperationName(RequestOperationName.DELETE.name());
		operationStatus.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return operationStatus;
		
		
		
	}

}
