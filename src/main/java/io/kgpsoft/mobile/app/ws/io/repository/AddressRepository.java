package io.kgpsoft.mobile.app.ws.io.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.kgpsoft.mobile.app.ws.io.entity.AddressEntity;
import io.kgpsoft.mobile.app.ws.io.entity.UserEntity;
import io.kgpsoft.mobile.app.ws.ui.model.response.UserResponse;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String id);
}
