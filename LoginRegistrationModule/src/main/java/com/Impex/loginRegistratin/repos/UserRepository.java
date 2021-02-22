package com.Impex.loginRegistratin.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Impex.loginRegistratin.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
}
