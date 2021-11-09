package com.Project.Project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Project.Project.Entity.Gig;
import com.Project.Project.Entity.Image;
import com.Project.Project.Entity.Request;
import com.Project.Project.Entity.Role;
import com.Project.Project.Entity.Test;
import com.Project.Project.Entity.User;
import com.Project.Project.Repository.GigRepository;
import com.Project.Project.Repository.ImageRepository;
import com.Project.Project.Repository.RequestRepository;
import com.Project.Project.Repository.TestRepository;
import com.Project.Project.Repository.UserRepository;
import com.Project.Project.Service.RoleService;
import com.Project.Project.Service.UserService;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)

public class ProjectApplication  implements CommandLineRunner{

	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RequestRepository requestRepo;
	
	@Autowired
	GigRepository serviceRepo;
	
	@Autowired
	TestRepository testRepo;
	
	@Autowired
	ImageRepository imageRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		//-----------------------------------------------------------------Roles---------------------------------------------------------------
		

		Role adminRole=null;
		Role teacherRole=null;
		Role coachRole=null;
		Role userRole=null;
		
		adminRole = roleService.newRole(new Role(Long.valueOf(1),"ADMIN"));
		teacherRole = roleService.newRole(new Role(Long.valueOf(2),"TEACHER"));
		coachRole = roleService.newRole(new Role(Long.valueOf(3),"COACH"));
		userRole = roleService.newRole(new Role(Long.valueOf(4),"USER"));
		
		
		//-----------------------------------------------------------------Declarations---------------------------------------------------------------
		
		User admin;
		User teacher;
		User coach;
		User user ;
		
		Image image;
		Request request ;
		
		BCryptPasswordEncoder encoder;
		
		List<Request> requests = new ArrayList<>();
		List<Gig> services = new ArrayList<>();
		List<Test> tests = new ArrayList<>();
		
		//-----------------------------------------------------------------User---------------------------------------------------------------
		
		
		user = new User ();
		
		user.setId(Long.valueOf(1));
		user.setUserName("layes");
		user.setPrenom("Ilyes");
		user.setNom("sahbeni");
		user.setEmail("layes@gmail.com");
		user.setPhone("58756898");
		user.setAdresse("Bizerte");
		user.setCurrentPost("Etudiant");
		encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode("12345"));
		user.getRoles().add(userRole);
		
		user.setMale(true);
		
		image = new Image();
		
		if(user.isMale()) {
			image.setPath("/images/usersImages/profile/male-avatar.jpg");//default profile image
		}else {
			image.setPath("/images/usersImages/profile/female-avatar.jpg");//default profile image
		}

		image.setUser(user);
		
		user.setImage(image);
		userRepo.save(user);
		
		
		for(int i=1 ; i<10 ; i++) {
			request = new Request(); 
			request.setId(Long.valueOf(i));
			request.setTitre("RequestTitre" + i);
			request.setDescription("blah blah" + i);
			request.setDeadLine( "" + new Date());
			request.setEnabled(true);
			request.setUser(userRepo.findByUserName("layes").get());
			
			image = new Image();
			image.setId(Long.valueOf(i+1));
			image.setPath("/images/requestsImages/thumbnail.jpg");//default image
			image.setRequest(request);
			
			request.setImage(image);

			requests.add(request);
		}
		

		user.setRequests(requests);
		userRepo.save(user);
		
		//-----------------------------------------------------------------Coach---------------------------------------------------------------
		
		coach = new User();
		
		coach.setId(Long.valueOf(2));
		coach.setUserName("lwes");
		coach.setPrenom("oussema"); 
		coach.setNom("bjuaoi");
		coach.setEmail("lwes@gmail.com");
		coach.setPhone("50262899");
		coach.setAdresse("Tunis");
		coach.setCurrentPost("Front End Developper");
		encoder = new BCryptPasswordEncoder();
		coach.setPassword(encoder.encode("12345"));
		coach.getRoles().add(coachRole);
		coach.getRoles().add(userRole);
		
		coach.setMale(true);
		
		image = new Image();
		
		if(coach.isMale()) {
			image.setPath("/images/usersImages/profile/male-avatar.jpg");//default image
		}else {
			image.setPath("/images/usersImages/profile/female-avatar.jpg");//default image
		}

		image.setUser(coach);
		
		coach.setImage(image);
		
		userRepo.save(coach);
		
		Gig service ;
		for(int i =1 ;i<10;i++) {
			
			service = new Gig();
			service.setId(Long.valueOf(i));
			service.setEnabled(true);
			service.setTitre("TitreService"+i);
			service.setDescription("blahblah " + i);
			service.setPrix(i*i);
			service.setUser(userRepo.findByUserName("lwes").get());
			
			image = new Image();
			image.setId(Long.valueOf(i+10));
			image.setPath("/images/servicesImages/thumbnail.jpg");//default image
			image.setGig(service);
			
			service.setImage(image);

			services.add(service);
		}
		
		coach.setServices(services);
		userRepo.save(coach);
		
		//-----------------------------------------------------------------Teacher----------------------------------------------------------------
		
		teacher = new User();
		 
		teacher.setId(Long.valueOf(3));
		teacher.setUserName("med");
		teacher.setPrenom("med");
		teacher.setNom("ouali");
		teacher.setEmail("med@gmail.com");
		teacher.setPhone("74185296");
		teacher.setAdresse("Tunis");
		teacher.setCurrentPost("Full Stack Developper");
		encoder = new BCryptPasswordEncoder();
		teacher.setPassword(encoder.encode("12345"));
		teacher.getRoles().add(teacherRole);
		
		
		teacher.setMale(true);
		
		image = new Image();
		
		if(teacher.isMale()) {
			image.setPath("/images/usersImages/profile/male-avatar.jpg");//default image
		}else {
			image.setPath("/images/usersImages/profile/female-avatar.jpg");//default image
		}

		image.setUser(teacher);
		
		teacher.setImage(image);
		
		userRepo.save(teacher);
		
		Test test;
		for(int i =1 ;i<10;i++) {
			
			test = new Test();
			test.setId(Long.valueOf(i));
			test.setEnabled(true);
			test.setTitre("testTitreTest"+i);
			test.setDescription("blahblah " + i);
			test.setPrix(i*i);
			test.setUser(userRepo.findByUserName("med").get());
			
			image = new Image();
			image.setId(Long.valueOf(i+20));
			image.setPath("/images/TestsImages/thumbnail.jpg");//default image
			image.setTest(test);
			
			test.setImage(image);
			
			tests.add(test);
		}
		
		teacher.setTests(tests);
		userRepo.save(teacher);
		
		//-----------------------------------------------------------------ADMIN---------------------------------------------------------------
		
		admin = new User();
		
		admin.setId(Long.valueOf(4));
		admin.setUserName("zed");
		admin.setPrenom("Firas");
		admin.setNom("Karbich");
		admin.setEmail("firaskarbich@gmail.com");
		admin.setPhone("65432180");
		admin.setAdresse("Tunis");
		admin.setCurrentPost("Back End Developper");
		encoder = new BCryptPasswordEncoder();
		admin.setPassword(encoder.encode("12345"));
		admin.getRoles().add(adminRole);
		admin.getRoles().add(userRole);
		
		
		user.setMale(false);
		
		image = new Image();
		
		if(admin.isMale()) {
			image.setPath("/images/usersImages/profile/male-avatar.jpg");//default image
		}else {
			image.setPath("/images/usersImages/profile/female-avatar.jpg");//default image
		}

		image.setUser(admin);
		
		admin.setImage(image);
		
		userRepo.save(admin);
		

		
	}

}
