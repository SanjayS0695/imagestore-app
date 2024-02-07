package com.codecademy.imagestore.repository;

import com.codecademy.imagestore.entity.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserData, Integer> {

    Optional<UserData> findByEmail(String email);
}
