package io.kgpsoft.mobile.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.kgpsoft.mobile.app.ws.io.entity.AddressEntity;
import io.kgpsoft.mobile.app.ws.io.entity.UserEntity;
import io.kgpsoft.mobile.app.ws.io.repository.AddressRepository;
import io.kgpsoft.mobile.app.ws.io.repository.UserRepository;
import io.kgpsoft.mobile.app.ws.service.AddressesService;
import io.kgpsoft.mobile.app.ws.shared.dto.AddressDto;


@Service
public class AddressesServiceImpl implements AddressesService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		
		List<AddressDto> addresses = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) {
			return addresses;
		}
		 
		Iterable<AddressEntity> addressEntityList =addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity : addressEntityList) {
			
			addresses.add(new ModelMapper().map(addressEntity,AddressDto.class));
		}
		return addresses;
	}

	@Override
	public AddressDto getUserAddresses(String addressId) {
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity != null) {
			
			return new ModelMapper().map(addressEntity,AddressDto.class);
		}
		
		
		return null;
	}

}
