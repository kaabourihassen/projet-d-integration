package com.Project.Project.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Gig;
import com.Project.Project.Entity.Image;
import com.Project.Project.Entity.Role;
import com.Project.Project.Entity.ServicesRates;
import com.Project.Project.Entity.User;

import com.Project.Project.Repository.RoleRepository;
import com.Project.Project.Repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	GigService gigService;
	
	//GET ALL USERS IN THE SYSTEM
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}

	//GET ONE USER BY ID
	public User getUserById(Long userId) {
		return userRepo.findById(userId).get();
	}
	
	//DELETE USER
	public void deleteUser(Long userId) {
		userRepo.deleteById(userId);
	}
	
	//UPDATE USER
	public User updateUser(User user) {
		return userRepo.save(user);
	}
	
	//REGISTER USER
	public void registerUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String Password = user.getPassword();
		String encodedPassword = encoder.encode(Password);
		
		user.setPassword(encodedPassword);
		
		Role userRole = roleRepo.findByName("USER");
		
		user.getRoles().add(userRole);
		
		Image image = new Image();
		image.setUser(user);
		if(user.isMale()) {
			image.setPath("/images/usersImages/profile/male-avatar.jpg");//default image
		}else {
			image.setPath("/images/usersImages/profile/female-avatar.jpg");//default image
		}
		user.setImage(image);
		
		userRepo.save(user);
	}
	
	//CURRENT LOGED IN USER
	public User getLoggedUser()throws Exception {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    String username = loggedInUser.getName(); 

	    User user = userRepo.findByUserName(username).get();
	    
	   return user;
		
	}
	
	public void rateGig(User user) {
		
		User newUser = new User();
		newUser.setId(user.getId());
		newUser.setNom(user.getNom());
		newUser.setPrenom(user.getPrenom());
		newUser.setPassword(user.getPassword());
		newUser.setEmail(user.getEmail());
		newUser.setEnabled(user.isEnabled());
		newUser.setUserName(user.getUserName());
		
		newUser.setServices(user.getServices());
		newUser.setRequests(user.getRequests());
		newUser.setRoles(user.getRoles());
		newUser.setTestDemands(user.getTestDemands());
		newUser.setTests(user.getTests());
		newUser.setImage(user.getImage());
		
		newUser.getServicesRated().addAll((user.getServicesRated()
				.stream()
				.map(ratedService -> {
					
					Gig gig = gigService.getGigById(ratedService.getRatedService().getId());
					
					ServicesRates newRatedService = new ServicesRates();
					newRatedService.setRatedService(gig);
					newRatedService.setRater(newUser);
					//System.out.println("die " + newRatedService.getRater().toString());
					newRatedService.setNbreStars(ratedService.getNbreStars());
					return ratedService;
				})
				.collect(Collectors.toList())
				));
		userRepo.save(newUser);
	}
	
	//ENABLE USER
	
	//DESABLE USER
}
