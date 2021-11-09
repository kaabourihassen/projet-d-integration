package com.Project.Project.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Project.Project.Entity.Role;
import com.Project.Project.Entity.User;

public class MyUserDetails implements UserDetails {

	private User user;
	
	public MyUserDetails(User user) {
		this.user=user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList <>();
		try {
			Set<Role> roles = user.getRoles();
			authorities = new ArrayList <>();
			
			for ( Role role : roles ) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
			
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

}
