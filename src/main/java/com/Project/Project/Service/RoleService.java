package com.Project.Project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Role;
import com.Project.Project.Repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRep;
	
	public Role newRole(Role r) {
		return roleRep.save(r);
	}
	
	public Role getRoleByName(String name) {
		return roleRep.findByName(name);
	}
}
