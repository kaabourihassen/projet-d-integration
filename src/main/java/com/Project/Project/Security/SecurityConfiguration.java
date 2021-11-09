package com.Project.Project.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		try {
			authProvider.setPasswordEncoder(passwordEncoder());
			authProvider.setUserDetailsService(userDetailsService());
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.authenticationProvider(authenticationProvider());
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		try {
			http.authorizeRequests()
			.antMatchers("register").anonymous()
			.antMatchers("/User/**").hasAnyAuthority("USER","COACH","ADMIN")
			.antMatchers("/Coach/**").hasAnyAuthority("COACH","ADMIN")
			.antMatchers("/Teacher/**").hasAnyAuthority("TEACHER","ADMIN")
			.antMatchers("/Admin/**").hasAuthority("ADMIN")
			.antMatchers("/requests/adminRequests/**").hasAuthority("ADMIN")
			.antMatchers("/services/adminServices/**").hasAuthority("ADMIN")
			.antMatchers("/tests/adminTests/**").hasAuthority("ADMIN")
			.antMatchers("home").anonymous()
			.antMatchers("profile/**").authenticated()
			.and()
			.formLogin()
			.loginPage("/login").permitAll()//.successForwardUrl("/home")
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
	}
	
}
