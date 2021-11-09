package com.Project.Project.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Project.Project.Entity.Gig;
import com.Project.Project.Entity.Image;
import com.Project.Project.Entity.Request;
import com.Project.Project.Entity.Role;
import com.Project.Project.Entity.ServicesRates;
import com.Project.Project.Entity.Test;
import com.Project.Project.Entity.User;
import com.Project.Project.Service.GigService;
import com.Project.Project.Service.ImageService;
import com.Project.Project.Service.RequestService;
import com.Project.Project.Service.RoleService;
import com.Project.Project.Service.ServicesRatesService;
import com.Project.Project.Service.TestService;
import com.Project.Project.Service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	GigService gigService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	ImageService imgService;
	
	@Autowired
	TestService testService;
	
	@Autowired
	ServicesRatesService srService;

	@Autowired
	RoleService roleService; 
	
	//ADD NEW REQUEST
	//Getting new Request form
	@RequestMapping("User/newRequest")
	public String getNewRequestForm( Model model) throws Exception {
		try{
			Request request=new Request();
			User userData=userService.getLoggedUser();

			request.setUser(userData);
			
			model.addAttribute("request", request);	
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return "User/newRequest";
	}
	
	//adding new request to the db
	@RequestMapping(value="User/addRequest" , method=RequestMethod.POST)
	public String addNewRequest(@ModelAttribute("request") Request request,@RequestParam("file") MultipartFile file, Model model) throws Exception {
		try {
			Image img=new Image();
			User userData=userService.getLoggedUser();
			
			Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\requestsImages\\",file.getOriginalFilename());
			//System.out.println("die : "+pathToFile.toAbsolutePath());
			
			request.setUser(userData);
			Request req=requestService.newRequest(request);
			
			if(file.getOriginalFilename().isEmpty()) {
				img.setPath("/images/requestsImages/thumbnail.jpg");//default image
			}else {
				img.setPath("/images/requestsImages/"+file.getOriginalFilename());	
				Files.write(pathToFile, file.getBytes());
			}

			img.setRequest(req);
			imgService.newImage(img);
			
			model.addAttribute("request" , req);
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "redirect:/User/myRequests";
	}

	//My requests
	@RequestMapping("User/myRequests")
	public String getMyRequests( Model model) throws Exception {
		try {
			User user=userService.getLoggedUser();
			
			List<Request> enabledRequests = new ArrayList<>();
			List<Request> disabledRequests = new ArrayList<>();
			
			for(Request request : user.getRequests()) {
				if(request.isEnabled()) {
					enabledRequests.add(request);
				}else {
					disabledRequests.add(request);
				}
			}
			
			model.addAttribute("enabledRequests", enabledRequests);
			model.addAttribute("disabledRequests", disabledRequests);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "User/myRequests";
	}
	
	
	//DELETE REQUEST
	@RequestMapping("User/deleteRequest/{requestId}")
	public String deleteRequest(@PathVariable(name="requestId")Long requestId , RedirectAttributes redirectAttributes) throws Exception {
		try {
			User user=userService.getLoggedUser();//current loged in user
			Request request = requestService.getRequestById(requestId);// request ili bch titfase5

			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if( user.getRoles().contains(adminRole) || request.getUser().getId() == user.getId()) {//if this request is owned by the current user
				if(request.getImage() != null) {//itha ken 3andha taswira kawa9tha yfase5ha wba3ed yet3adda yfasse5 request (Contraint d'integriter )
					
					imgService.deleteImage(request.getImage().getId());	
					
				}
				
				requestService.deleteRequest(requestId);
				
				try {
					if(requestService.getRequestById(requestId) != null ) {
						
						redirectAttributes.addFlashAttribute("Failed" , "Failed to delete test : " + request.getTitre());
					}

				}catch(Exception e) {
					System.out.println(e.getMessage());
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly deleted test : " + request.getTitre());
				}
				
				if(request.getUser().getId() == user.getId() ) {
					return "redirect:/User/myRequests";	
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/requests/adminRequests";
					}
				}
				return "redirect:/User/myRequests";	
				
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return "error/403";
	}
	
	
	//UPDATE REQUEST
	//Get edit request form
	@RequestMapping("User/editRequest/{requestId}")
	public String getEditRequestForm(@PathVariable(name="requestId")Long requestId , Model model) throws Exception {
		try {
			Request request=requestService.getRequestById(requestId);
			User user=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(request.getUser().getId() == user.getId() || user.getRoles().contains(adminRole) ) {//if this request is owned by the current loged in user or he has "ADMIN" role 
					model.addAttribute("request", request);
					return "User/editRequest";		
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return "error/403";//U Have no acces !(request mich mte3ou wela t7ayil mil url ,..)
	}
	
	//Update the request 
	@RequestMapping(value="User/updateRequest/{requestId}" , method= RequestMethod.POST)
	public String updateRequest(@PathVariable(name="requestId") Long requestId , @ModelAttribute(name="request") Request request , @RequestParam("file") MultipartFile file , Model model ,  RedirectAttributes redirectAttributes) throws Exception {
		
		try {
			Image img=new Image();
			Request r = requestService.getRequestById(requestId);
			
			img.setId(r.getImage().getId());
			request.setId(requestId);
			User user = userService.getLoggedUser();//getting all user info before updating the request
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(user.getRoles().contains(adminRole) || user.getId() == r.getUser().getId()) {
				
				if(user.getRoles().contains(adminRole)) {
					request.setUser(r.getUser());
				}else {
					request.setUser(user);
				}
				
				Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\requestsImages\\",file.getOriginalFilename());
				
				if(file.getOriginalFilename().isEmpty()) {
				request.setImage(r.getImage());
					img.setPath("/images/RequestsImages/thumbnail.jpg");//default image
				}else {
					img.setRequest(request);
					img.setPath("/images/RequestsImages/"+file.getOriginalFilename());	
					Files.write(pathToFile, file.getBytes());
					imgService.updateImage(img);
				}
				
				requestService.updateRequest(request);//updating request
				
				Request updated = null;
				updated = requestService.updateRequest(request);//updating request
				
				if(updated.getId() == null ) {//means failed to delete the test
					
					redirectAttributes.addFlashAttribute("Failed" , "Failed to update service : " + r.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly updated service : " + r.getTitre());
				}
				
				
				if(user.getId() == r.getUser().getId()) {
					return "redirect:/User/myRequests";
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/requests/"+"adminRequests";
					}
				}
				
				return "redirect:/User/myRequests";
				
			}
		}catch(Exception e) {
			System.out.println("die" + e.getMessage());
		}
		return "/error/403";
	}
	
	//ENABLE REQUEST
	@RequestMapping("/User/enableRequest/{requestId}")
	public String enableRequest(@PathVariable(name="requestId")Long requestId , RedirectAttributes redirectAttributes) {
		
		try {
			User user = userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Request request = requestService.getRequestById(requestId);
			
			if(user.getRoles().contains(adminRole) || user.getId() == request.getUser().getId()) {
				request.setEnabled(true);
				requestService.updateRequest(request);	
				
				if(requestService.getRequestById(requestId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly enabled request : " + request.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to enable request : " + request.getTitre());
				}
				
			}
			
			if(user.getId() == request.getUser().getId()) {
				return "redirect:/User/myRequests";
			}else {
				if(user.getRoles().contains(adminRole) ) {
					return "redirect:/requests/adminRequests";
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//getting all user info before updating the request
		
		
		
		return "redirect:/User/myRequests";
	}
	//DISABLE REQUEST
	@RequestMapping("/User/disableRequest/{requestId}")
	public String disbleRequest(@PathVariable(name="requestId")Long requestId , RedirectAttributes redirectAttributes) {
		
		try {
			User user = userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Request request = requestService.getRequestById(requestId);
			
			
			if(user.getRoles().contains(adminRole) || user.getId() == request.getUser().getId()) {
				request.setEnabled(false);
				requestService.updateRequest(request);
				
				if(!requestService.getRequestById(requestId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly disabled request : " + request.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to disable request : " + request.getTitre());
				}
			}
			
			if(user.getId() == request.getUser().getId()) {
				return "redirect:/User/myRequests";
			}else {
				if(user.getRoles().contains(adminRole) ) {
					return "redirect:/requests/adminRequests";
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "redirect:/User/myRequests";
	}
	
	// NEW DEMAND 
	@RequestMapping("/User/newDemand/{testId}")
	public String newDemand(@PathVariable(name="testId") Long testId , Model model) {
		
		try {
			Test test = testService.getTestById(testId);// test  
			User user = userService.getLoggedUser();//current logged in user
			
			user.getTestDemands().add(test); // zidna test ili talbou fil testDemands mta3 lcurrent logged in user
			test.getUsers().add(user); // zidna luser lil liste des users ta3 test (ili fiha les users ili 3amlou demand 3le test)
			
			//updating logged in user info 
			userService.updateUser(user);
			
			User testOwner = userService.getUserById(test.getUser().getId()); // teacher moula test ili sar 3liha idemand
			
			testOwner.getTestDemands().add(test); // zidna itest ili je 3lih demand fil liste des testDemands mta3 teacher ili houwa testOwner
			
			//updating testOwner info 
			//userService.updateUser(testOwner);
			
			model.addAttribute("user", user); 
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/User/demandedTests" ;
	}
	
	//MY TESTS DEMANDS
	@RequestMapping("/User/demandedTests")
	public String getMyTestsDemands(Model model) {
		Set<Test> enabledTestsDemands = new HashSet<>(); //les demandes des tests qu'ils sont enabled.
		try {
			
			User user = userService.getLoggedUser();//current logged in user
			
			for(Test t : user.getTestDemands()) {
				if(t.isEnabled()) {
					enabledTestsDemands.add(t);
				}
			}
			
			model.addAttribute("TestsDemands", enabledTestsDemands);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "/User/demandedTests";
	}
	
	//DELETE DEMANDED TEST
	@RequestMapping("/User/deleteDemandedTest/{demandedTestId}")
	public String deleteDemandedTest(@PathVariable(name="demandedTestId") Long demandedTestId , Model  model) {
		
		try {
			Test test = testService.getTestById(demandedTestId);
			User user = userService.getLoggedUser();
			
			User testOwner = userService.getUserById(test.getUser().getId()); // teacher moula test ili sar 3liha idemand
			
			testOwner.getTestDemands().remove(test);
			test.getUsers().remove(user);
			user.getTestDemands().remove(test);
			
			userService.updateUser(user);
			
			model.addAttribute("user", user); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/User/demandedTests" ;
	}
	
	//USER RATE SERVICE
	@RequestMapping("/User/rate/{gigId}")
	public String rateGig(@PathVariable(name="gigId") Long gigId ,Model model) throws Exception {
		
		try {
			User rater = userService.getLoggedUser();//current logged-in user <==> rater
			
			Gig ratedService = gigService.getGigById(gigId);
			
			
			
			if(!rater.getServices().contains(ratedService)) {
				ServicesRates sr = new ServicesRates();
				
				sr.setRatedService(ratedService);
				sr.setRater(rater);
				sr.setNbreStars(5);
				sr.getId().setRaterId(rater.getId());
				sr.getId().setServiceId(ratedService.getId());
				
				//ratedService.getRatedServices().add(sr);
				rater.getServicesRated().add(sr);
				//ratedService.getRatedServices().add(sr);
				
				userService.updateUser(rater);
				
				userService.rateGig(rater);
				
				//gigService.updateGig(ratedService);
				//srService.newServicesRates(sr);
				
				//model.addAttribute("gigs", gigService.getAllGigs());
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssss");
		}
		
		return "redirect:/services" ;
	}

}
