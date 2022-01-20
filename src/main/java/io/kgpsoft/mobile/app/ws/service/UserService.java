package io.kgpsoft.mobile.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	public UserDto createUser(UserDto user);
	
	public UserDto getUser(String email);

	public UserDto getUserByUserId(String userId);

	public UserDto updateUser(String userId, UserDto userDto);
	
	public void deleteUser(String userId);

	public List<UserDto> getUsers(int page, int limit);
}
