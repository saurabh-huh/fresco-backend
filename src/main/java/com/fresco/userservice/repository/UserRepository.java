package com.fresco.userservice.repository;

import com.fresco.userservice.model.dao.UserDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDao,String> {

    Optional<UserDao> findByEmail(String email);
}
