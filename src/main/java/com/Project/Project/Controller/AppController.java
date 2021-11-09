package com.Project.Project.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Project.Project.Entity.Gig;
import com.Project.Project.Entity.Image;
import com.Project.Project.Entity.Password;
import com.Project.Project.Entity.Request;
import com.Project.Project.Entity.ServicesRates;
import com.Project.Project.Entity.Test;
import com.Project.Project.Entity.User;
import com.Project.Project.Service.GigService;
import com.Project.Project.Service.ImageService;
import com.Project.Project.Service.RequestService;
import com.Project.Project.Service.TestService;
import com.Project.Project.Service.UserService;

@Controller
public class AppController {

	@Autowired
	UserService userService;
	
	@Autowired
	TestService testService;
	
	@Autowired
	ImageService imgService;
	
	@Autowired
	GigService gigService;
	
	@Autowired
	RequestService requestService;
	
	@RequestMapping("")
	public  String home() {
		return "redirect:/home";
	}
	
	@RequestMapping("/home")
	public String HomePage(Model model) {
		
		try {
			User user = userService.getLoggedUser();
			
			model.addAttribute("user", user);
			model.addAttribute("profileImage", user.getImage().getPath());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "home";
	}

	@RequestMapping("/login")
	public String getLoginForm() {
		return "login";
	}
	
	@RequestMapping("/register")
	public String getSignUpForm(Model model) {
		try {
			User user=new User();
			model.addAttribute("user", user);
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "register";
	}
	
	@RequestMapping(value="/addUser" , method = RequestMethod.POST)
	public String addUser(@ModelAttribute("user") User user) {
		try {
			userService.registerUser(user);
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return "redirect:/";
	}
	
	@RequestMapping(value= {"/profile" , "/profile/{userId}"} , method=RequestMethod.GET)
	public String getProfileView(@PathVariable(name="userId" , required=false) Long userId , Model model, RedirectAttributes redirectAttributes) {
		User user = new User();
		System.out.println("$$" + userId);
		User logedInUser = new User();
		//current loged in user profile

		try {
			if(userId != null) {//other users profile
				user = userService.getUserById(userId) ;
			}else {
				user = userService.getUserById((Long) redirectAttributes.getAttribute("userId")) ;
			}
			logedInUser = userService.getLoggedUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user.getId() != null || logedInUser.getId() != null) {
			model.addAttribute("user", user );
			model.addAttribute("logedInUser", logedInUser );
			return "/profile";
		}
		
		//return "error/404";
		return "redirect:/login";
	}
	
	@RequestMapping("/profile/editProfile/{userId}")
	public String getEditProfileForm(@PathVariable(name="userId")Long userId , Model model) {
		User logedInUser = new User();
		try {
			logedInUser = userService.getLoggedUser();

		} catch (Exception e) {
			
			e.printStackTrace();
			
			return "error/403";
		}
		
		User user = new User();
		
		try {
			user = userService.getUserById(userId) ;
		}catch(Exception e) {
			
			e.printStackTrace();
			return "error/404";
		}
		
		if(user.getId() == logedInUser.getId()) {
			model.addAttribute("user", user);
			return "editProfile";
		}
		
		return "error/403";
	}
	
	@RequestMapping(value="/updateProfile/{userId}" , method = RequestMethod.POST)
	public String updateProfile(@PathVariable(name="userId")Long userId , @ModelAttribute("user") User userParam,@RequestParam("file") MultipartFile file , RedirectAttributes redirectAttributes) {
		
		User logedInUser = new User();
		try {
			logedInUser = userService.getLoggedUser();

		} catch (Exception e) {
			
			e.printStackTrace();
			
			return "error/403";
		}
		
		User user = new User();
		
		try {
			user = userService.getUserById(userId) ;
		}catch(Exception e) {
			
			e.printStackTrace();
			return "error/404";
		}
		
		if(user.getId() == logedInUser.getId()) {
			
			userParam.setId(user.getId());
			userParam.setRoles(user.getRoles());
			userParam.setServices(user.getServices());
			userParam.setRequests(user.getRequests());
			userParam.setTests(user.getTests());			
			userParam.setServicesRated(user.getServicesRated());
			userParam.setTestDemands(user.getTestDemands());
			userParam.setPassword(user.getPassword());
			
			Image img=new Image();
			
			Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\usersImages\\profile\\",file.getOriginalFilename());
			
			if(file.getOriginalFilename().isEmpty()) {
				//img.setPath("/images/requestsImages/thumbnail.jpg");//default image
			}else {
				img.setPath("/images/usersImages/profile/"+file.getOriginalFilename());	
				try {
					Files.write(pathToFile, file.getBytes());
					img.setUser(user);
					img.setId(user.getImage().getId());
					imgService.updateImage(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			User u =userService.updateUser(userParam);
			
			if( u != null ) {
				//success
				redirectAttributes.addFlashAttribute("Succes" , "Succesfuly updated your profile ");
			}else {
				//failed
				redirectAttributes.addFlashAttribute("Failed" , "Failed to updated your profile " );
			}
			
			redirectAttributes.addFlashAttribute("userId",userId);
			
			return "redirect:/profile";
		}
		
		return "error/403";
	}
	
	@RequestMapping(value="/profile/saveAndChangePassword" , method = RequestMethod.POST)
	public String saveAndChangePassword(@ModelAttribute(name="user")User user , @RequestParam("file") MultipartFile file , RedirectAttributes redirectAttributes) {
		 //@ModelAttribute("user") User userParam,@RequestParam("file") MultipartFile file , RedirectAttributes redirectAttributes
		
		updateProfile(user.getId() , user, file , redirectAttributes);
		
		return "redirect:/profile/changePassword/"+user.getId();
	}
	
	@RequestMapping("/profile/changePassword/{userId}")
	public String getChangePasswordForm(@PathVariable(name="userId")Long userId , Model model) {
		
		User logedInUser = new User();
		try {
			logedInUser = userService.getLoggedUser();

		} catch (Exception e) {
			
			e.printStackTrace();
			
			return "error/403";
		}
		
		User user = new User();
		
		try {
			user = userService.getUserById(userId) ;
		}catch(Exception e) {
			
			e.printStackTrace();
			return "error/404";
		}
		
		if(user.getId() == logedInUser.getId()) {
			
			model.addAttribute("password", new Password(user.getId()));
			
			return "changePassword";
		}
		
		return "error/403";
	}
	
	@RequestMapping(value="/profile/updatePassword/{userId}" , method=RequestMethod.POST)
	public String updatePassword(@PathVariable(name="userId")Long userId ,@ModelAttribute("password") Password pass , RedirectAttributes redirectAttributes) {
		
		User logedInUser = new User();
		try {
			logedInUser = userService.getLoggedUser();

		} catch (Exception e) {
			
			e.printStackTrace();
			
			return "error/403";
		}
		
		User user = new User();
		
		try {
			user = userService.getUserById(userId) ;
		}catch(Exception e) {
			
			e.printStackTrace();
			return "error/404";
		}
		
		if(user.getId() == logedInUser.getId()) {
			
			if(user.getId() == pass.getUserId()) {
				
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				
				if( encoder.matches( pass.getOldPassword() , user.getPassword()) ) {
					
					if(pass.isValidPassword()) {
						
						if(!encoder.matches(pass.getNewPassword(), user.getPassword())) {
							user.setPassword(encoder.encode(pass.getNewPassword()));
							User updated = null;
							updated = userService.updateUser(user);
							
							if(updated == null) {
								
								redirectAttributes.addFlashAttribute("Failed" , "Failed to update password : " + logedInUser.getUserName());
							}else {
								redirectAttributes.addFlashAttribute("Succes" , "Succesfuly updated password : " + logedInUser.getUserName());
							}
							
							return "redirect:/profile";
						}
						
						return "redirect:/profile/changePassword/"+logedInUser.getId();//raje3tou lil profile 5ater ma3raftech kifeh n3ayit lil logout
						
					}
				}
			}
			
		}
		
		return "redirect:/profile/changePassword/"+logedInUser.getId();//just nraj3ou lil lchange password ta3 lprofile mte3 lcurrent loged in user.
	}
	
	@RequestMapping(value= {"/tests" , "/tests/{destination}"} , method=RequestMethod.GET )
	public String getAllTests(@PathVariable(name="destination" , required=false)String destination , Model model ) {
		
		User user  = new User();
		try {
			user = userService.getLoggedUser();//current logged-in user
			model.addAttribute("user", user);
			model.addAttribute("profileImage", user.getImage().getPath());
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		try {
			List<Test> tests = testService.getAllTests();
			
			if(destination != null) {
				model.addAttribute("tests", tests);
				model.addAttribute("user", user);
				
				/*try {
					model.addAttribute("SuccesfulyDeletedTest" , params.get("SuccesfulyDeletedTest"));
				}catch(Exception e) {
					System.out.println("die :"+e.getMessage());
				}
				try {
					model.addAttribute("FailedToDeleteTest" , params.get("FailedToDeleteTest"));
				}catch(Exception e1) {
					System.out.println("die :"+e1.getMessage());
				}*/
				
				 /*String some = (String) model.asMap().get("SuccesfulyDeletedTest");
				 System.out.println("$$" + some);*/
				return "/Admin/"+destination;
			}
			
			List<Test> disabledTests = new ArrayList<>();
			
			//Removing all disabled tests
			for(Test test  : tests) {
				if(!test.isEnabled()) {
					disabledTests.add(test);
				}
			}
			tests.removeAll(disabledTests);
			
			try {
				tests.removeAll(user.getTests());//teacher maychoufech ltests mte3ou m3a les tests ta3 b9eyet teachers ychoufhom fi my tests mela 3leh 3amelha ena trah hahah
			}catch(Exception e) {
				System.out.println("die :"+e.getMessage());
			}
			
			try {
				tests.removeAll(user.getTestDemands());//user (user/coach) zeyid bch yraw tests ili 3amloulhom demands  , ynajm yrahom fi my demands , mella 3lech 3amelha ena haha
			}catch(Exception e) {
				System.out.println("die :"+e.getMessage());
			}
			
			model.addAttribute("tests", tests);
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "/tests";
	}
	
	@RequestMapping(value= { "/services" , "/services/{destination}" } , method=RequestMethod.GET)
	public String getAllServices(@PathVariable(name="destination" , required=false ) String destination , Model model ) {
		
		List<Gig> gigs=gigService.getAllGigs();
		User user  = new User();
		try {
			
			user = userService.getLoggedUser();//current logged-in user
			model.addAttribute("currentLoggedInUser", user);
			
			model.addAttribute("user", user);
			model.addAttribute("profileImage", user.getImage().getPath());
			
			gigs=gigService.getAllGigs();
			
			if( destination != null ) {
				model.addAttribute("gigs", gigs);
				return "/Admin/adminServices";
			}
			
			Collection<ServicesRates> servicesRated = user.getServicesRated();
			
			List<Gig> ratedGigs = new ArrayList<>();
			
			for(ServicesRates sr : servicesRated) {
				ratedGigs.add(sr.getRatedService());
			}
			
			
			
			gigs.removeAll(ratedGigs);
			
			gigs.removeAll(user.getServices());
			//System.out.println("$$$" + gigs.size());

			model.addAttribute("ratedServices", ratedGigs);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		List<Gig> disabledGigs = new ArrayList<>();
		
		//Removing all disabled services
		for(Gig gig  : gigs) {
			if(!gig.isEnabled()) {
				disabledGigs.add(gig);
			}
		}
		gigs.removeAll(disabledGigs);
		
		
		
		model.addAttribute("gigs", gigs);
		
		
		return "services";
	}
	
	//Get all requests
		@RequestMapping(value= {"/requests" , "/requests/{destination}"} , method=RequestMethod.GET)
		public String getAllRequests(Model model,@PathVariable(name="destination",required=false) String destination) {
			User user  = new User();
			try {
				user = userService.getLoggedUser();//current logged-in user
				model.addAttribute("user", user);
				model.addAttribute("profileImage", user.getImage().getPath());
			}catch(Exception e) {
				System.out.println("die :"+e.getMessage());
			}
			
			try {
				List<Request> requests = requestService.getAllRequests();
				
				if(destination != null) {
					model.addAttribute("requests", requests);
					model.addAttribute("user", user);
					return "/Admin/"+destination;
				}
				
				List<Request> disabledRequests = new ArrayList<>();
				
				//Removing all disabled requests
				for(Request request  : requests) {
					if(!request.isEnabled()) {
						disabledRequests.add(request);
					}
				}
				requests.removeAll(disabledRequests);
				
				try {
					requests.removeAll(user.getRequests());
				}catch(Exception e) {
					System.out.println("die :"+e.getMessage());
				}
				
				model.addAttribute("requests", requests);
			}catch(Exception e) {
				System.out.println("die :"+e.getMessage());
			}

			
			return "requests";
		}
		
		@RequestMapping("/search")
		public String search(@RequestParam(name="keyword")String keyword , Model model , RedirectAttributes redirectAttributes) {
			
			if(keyword.length() > 0) {
				model.addAttribute("gigs" , gigService.getServicesByKeuword(keyword));
				model.addAttribute("keyword",keyword);
				return "services";
			}
			return "redirect:/services";
		}
		
}






























