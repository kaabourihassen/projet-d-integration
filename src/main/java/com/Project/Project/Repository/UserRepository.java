package com.Project.Project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.Project.Project.Entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	/*@Query("SELECT u FROM users u WHERE u.username = :username")
	public User	getUserByUsername(@Param("username") String username);*/
	
	Optional<User> findByUserName(String UserName);
}
