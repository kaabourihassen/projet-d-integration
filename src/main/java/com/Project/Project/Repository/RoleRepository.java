package com.Project.Project.Repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Project.Project.Entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{

	Role findByName(String Name);
	
}
