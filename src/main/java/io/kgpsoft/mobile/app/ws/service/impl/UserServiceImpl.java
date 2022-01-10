package io.kgpsoft.mobile.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.kgpsoft.mobile.app.ws.io.entity.UserEntity;
import io.kgpsoft.mobile.app.ws.io.repository.UserRepository;
import io.kgpsoft.mobile.app.ws.service.UserService;
import io.kgpsoft.mobile.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDto createUser(UserDto user) {
		// TODO Auto-generated method stub
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setEncryptedPassword("test");
		userEntity.setUserId("testUserId");
		
		UserEntity createdUserEntity = userRepository.save(userEntity);
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(createdUserEntity, userDto);
		
		
		return userDto;
	}

}
