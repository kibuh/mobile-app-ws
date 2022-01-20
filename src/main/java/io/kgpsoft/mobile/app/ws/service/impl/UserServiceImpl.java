package io.kgpsoft.mobile.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.kgpsoft.mobile.app.ws.exceptions.UserServiceException;
import io.kgpsoft.mobile.app.ws.io.entity.UserEntity;
import io.kgpsoft.mobile.app.ws.io.repository.UserRepository;
import io.kgpsoft.mobile.app.ws.service.UserService;
import io.kgpsoft.mobile.app.ws.shared.Utils;
import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;
import io.kgpsoft.mobile.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils userUtils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		UserEntity storedEntity = userRepository.findByEmail(user.getEmail());
		if (storedEntity != null)
			throw new RuntimeException("Record already exists");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		String userId = userUtils.generateUserId(30);
		userEntity.setUserId(userId);

		UserEntity createdUserEntity = userRepository.save(userEntity);

		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(createdUserEntity, userDto);

		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(email);
		if (user == null)
			throw new UsernameNotFoundException(email);

		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {

		UserEntity user = userRepository.findByEmail(email);
		if (user == null)
			throw new UsernameNotFoundException(email);

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);

		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserDto userDto = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User  with "+ userId +"not found");

		BeanUtils.copyProperties(userEntity, userDto);

		return userDto;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {

		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserEntity updatedEntity = userRepository.save(userEntity);

		BeanUtils.copyProperties(updatedEntity, returnValue);

		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		
		
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> users = new ArrayList<>();
		
		Pageable pageableRequest =new  PageRequest(page, limit);
		
		Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
		
		for(UserEntity userEntity : userPage) {
			UserDto userDto = new UserDto();
			
			BeanUtils.copyProperties(userEntity,userDto);
			
			users.add(userDto);
		}
		return users;
	}

}
