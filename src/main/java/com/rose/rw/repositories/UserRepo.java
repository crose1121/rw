package com.rose.rw.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rose.rw.models.User;
@Repository
public interface UserRepo extends CrudRepository<User, Long> {
	List<User> findAll();
	//there are many more methods we can use to query the database
	Optional<User> findByEmail(String email);
}
