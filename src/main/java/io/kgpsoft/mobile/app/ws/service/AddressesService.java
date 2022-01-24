package io.kgpsoft.mobile.app.ws.service;

import java.util.List;

import io.kgpsoft.mobile.app.ws.shared.dto.AddressDto;

public interface AddressesService {
	
	List<AddressDto> getAddresses(String userId);

	AddressDto getUserAddresses(String addressId);

}
