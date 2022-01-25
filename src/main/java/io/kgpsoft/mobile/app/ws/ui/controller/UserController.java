package io.kgpsoft.mobile.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.kgpsoft.mobile.app.ws.exceptions.UserServiceException;
import io.kgpsoft.mobile.app.ws.service.AddressesService;
import io.kgpsoft.mobile.app.ws.service.UserService;
import io.kgpsoft.mobile.app.ws.shared.dto.AddressDto;
import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;
import io.kgpsoft.mobile.app.ws.ui.model.request.UserDetailsRequestModel;
import io.kgpsoft.mobile.app.ws.ui.model.response.AddressResponse;
import io.kgpsoft.mobile.app.ws.ui.model.response.ErrorMessages;
import io.kgpsoft.mobile.app.ws.ui.model.response.OperationStatusModel;
import io.kgpsoft.mobile.app.ws.ui.model.response.RequestOperationStatus;
import io.kgpsoft.mobile.app.ws.ui.model.response.UserResponse;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressesService addressesService;

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

//		UserResponse returnUser = new UserResponse();

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper mapper = new ModelMapper();

		UserDto userDto = mapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);

//		 BeanUtils.copyProperties(createdUser, returnUser);
		UserResponse returnUser = mapper.map(createdUser, UserResponse.class);

		return returnUser;
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit) {

		List<UserResponse> returnUsers = new ArrayList<UserResponse>();

		List<UserDto> userDtoList = userService.getUsers(page, limit);

		ModelMapper modelMapper = new ModelMapper();

		for (UserDto userDto : userDtoList) {

//			UserResponse userResponse = new UserResponse();

//			BeanUtils.copyProperties(userDto, userResponse);

			UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);

			returnUsers.add(userResponse);
		}
		return returnUsers;
	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponse getUser(@PathVariable String id) {

//		UserResponse returnUser = new UserResponse();
		UserDto userDto = userService.getUserByUserId(id);

		ModelMapper modelMapper = new ModelMapper();

//		BeanUtils.copyProperties(userDto, returnUser);
		UserResponse returnUser = modelMapper.map(userDto, UserResponse.class);
		return returnUser;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

//		UserResponse returnUser = new UserResponse();

//		UserDto userDto = new UserDto();

		ModelMapper modelMapper = new ModelMapper();

//		BeanUtils.copyProperties(userDetails, userDto);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto updatedUser = userService.updateUser(id, userDto);

//		BeanUtils.copyProperties(updatedUser, returnUser);

		UserResponse returnUser = modelMapper.map(updatedUser, UserResponse.class);

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

	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<AddressResponse> getUserAddresses(@PathVariable String id) {

//		UserResponse returnUser = new UserResponse();
		List<AddressDto> addressesDtoList = addressesService.getAddresses(id);

		ModelMapper modelMapper = new ModelMapper();

//		BeanUtils.copyProperties(userDto, returnUser);
		List<AddressResponse> returnAddresses = new ArrayList<AddressResponse>();

		if (addressesDtoList != null && !addressesDtoList.isEmpty()) {
			for (AddressDto addressDto : addressesDtoList) {

				returnAddresses.add(modelMapper.map(addressDto, AddressResponse.class));
			}

//			or

//			java.lang.reflect.Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
//			returnAddresses = new ModelMapper().map(addressesDtoList, listType);

			
			for (AddressResponse addressRest : returnAddresses) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
						.withSelfRel();
				addressRest.add(addressLink);

				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				addressRest.add(userLink);
			}
		}
		
		

		return new CollectionModel<>(returnAddresses);
	}
	
	
	
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE, "application/hal+json" })
	public EntityModel<AddressResponse> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

		AddressDto addressesDto = addressesService.getUserAddresses(addressId);

		ModelMapper modelMapper = new ModelMapper();
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

		AddressResponse addressesRestModel = modelMapper.map(addressesDto, AddressResponse.class);

		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);
		

		 
		return new EntityModel<>(addressesRestModel);
	}

}
