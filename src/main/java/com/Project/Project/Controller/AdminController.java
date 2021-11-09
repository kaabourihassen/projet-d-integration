package com.Project.Project.Controller;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

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
import com.Project.Project.Service.TestService;
import com.Project.Project.Service.UserService;

@Controller
@Transactional
public class AdminController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	GigService gigService;
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	TestService testService;
	
	@RequestMapping("/Admin/adminDashboard" )
	public String getAdminDashboard() {
		return "Admin/adminDashboard";
	}
	
	@RequestMapping(value= {"/Admin/adminRequests"} )
	public String getAllRequests() {
		return "redirect:/requests/" + "adminRequests";
	}
	
	@RequestMapping("/Admin/adminServices")
	public String getAllServices() {
		return "redirect:/services/"+"adminServices";
	}
	
	@RequestMapping("/Admin/adminTests")
	public String getAllTests() {
		return "redirect:/tests/"+"adminTests";
	}
	
	@RequestMapping("/Admin/adminAccounts")
	public String getAllAccounts(Model model) {

		try {
			User user=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(user.getRoles().contains(adminRole)) {
				List<User> users = userService.getAllUsers();
				
				users.remove(user);//removing current ADMIN logedIn account from the list
				
				
				model.addAttribute("users", users);
			}else {
				return "error/403";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/Admin/adminAccounts";
	}
	
	@RequestMapping("/Admin/enableAccount/{userId}")
	public String enableUser(@PathVariable(name="userId")Long userId , RedirectAttributes redirectAttributes) {
		
		try {
			User logedUser=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(logedUser.getRoles().contains(adminRole)) {
				
				User user = userService.getUserById(userId);
				
				user.setEnabled(true);
				userService.updateUser(user);
				
				if(userService.getUserById(userId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly enabled user : " + user.getUserName());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to enable user : " + user.getUserName());
				}
				
				
				return "redirect:/Admin/adminAccounts";
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "error/403";
	}
	
	@RequestMapping("/Admin/disableAccount/{userId}")
	public String disableUser(@PathVariable(name="userId")Long userId , RedirectAttributes redirectAttributes) {
		
		try {
			User logedUser=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(logedUser.getRoles().contains(adminRole)) {
				
				User user = userService.getUserById(userId);
				
				user.setEnabled(false);
				userService.updateUser(user);
				

				if(!userService.getUserById(userId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly disabled user : " + user.getUserName());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to disable user : " + user.getUserName());
				}
				
				
				return "redirect:/Admin/adminAccounts";
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "error/403";
	}

	@RequestMapping("/Admin/deleteUser/{userId}")
	public String deleteUser(@PathVariable(name="userId")Long userId , RedirectAttributes redirectAttributes) {
		
		try {
			User logedUser=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(logedUser.getRoles().contains(adminRole)) {
				
				User user = userService.getUserById(userId);
				
				if(user != null) {
					Role teacherRole = roleService.getRoleByName("TEACHER");
					Role coeachRole = roleService.getRoleByName("COACH");
					Role userRole = roleService.getRoleByName("USER");
					
					//deletin test demands
					if(user.getTestDemands()!=null && user.getTestDemands().size() > 0) {
						
						for(Test t : user.getTestDemands()) {
							t.getUsers().remove(user);
						}
						user.setTestDemands(null);
					}
					
					
					//deletin profile image
					imageService.deleteImage(user.getImage().getId());
					user.setImage(null);
					
					//deletin test
					if(user.getTests()!=null && user.getTests().size()>0) {
						for(Test t : user.getTests()) {
							
							//deletin the  demands
							if(t.getUsers().size()>0) {
								for(User demander : t.getUsers()) {
									demander.getTestDemands().remove(t);
								}
								t.setUsers(null);
							}

							//deletin test image
							imageService.deleteImage(t.getImage().getId());
							t.setImage(null);
							
							t.setUser(null);
							testService.deleteTestById(t.getId());
						}
						
						user.setTests(null);
						
						//userService.updateUser(user);
					}
					
					
					
					
					// deleteing roles relation 
					for(Role role : user.getRoles()) {
						role.getUsers().remove(user);
					}
					user.setRoles(null);

					userService.deleteUser(userId);

					try {
						if(userService.getUserById(userId) != null ) {
							
							redirectAttributes.addFlashAttribute("Failed" , "Failed to delete test : " + user.getUserName());
						}

					}catch(Exception e) {
						System.out.println(e.getMessage());
						redirectAttributes.addFlashAttribute("Succes" , "Succesfuly deleted test : " + user.getUserName());
					}
					
					return "redirect:/Admin/adminAccounts";
				}

			}
			//tnajem 7atta disactivilou laacount lehne !! bch yitrabba hh
			return "error/403";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return "error/403";
	}
	
	/*@RequestMapping("/Admin/editUser/{userId}")
	public String getEditUserForm(@PathVariable(name="userId")Long userId , Model model) {
		
		try {
			User logedUser=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(logedUser.getRoles().contains(adminRole)) {//na3ref ili houwa mawsol lihne ken deja mlogini wrole mte3ou admin but chkoun ya3ref 3adenya .
			
				User user = userService.getUserById(userId);
				
				model.addAttribute("user", user);
				
				return "/Admin/editUser";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "error/403";
		
	}
	
	@RequestMapping("/Admin/updateUser/{userId}")
	public String updateUser(@PathVariable(name="userId")Long userId ,@ModelAttribute(name="user") User userFromParam , @RequestParam("file") MultipartFile file , Model model) {
		
		try {
			User logedUser=userService.getLoggedUser();
			User user = userService.getUserById(userId);
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if( logedUser.getRoles().contains(adminRole) || user.getId() == logedUser.getId()) {
				
				userFromParam.setRoles(user.getRoles());
				userFromParam.setId(userId);
				userFromParam.setServices(user.getServices());
				userFromParam.setServicesRated(user.getServicesRated());
				userFromParam.setRequests(user.getRequests());
				userFromParam.setTestDemands(user.getTestDemands());
				userFromParam.setTests(user.getTests());
				userFromParam.setImage(user.getImage());
				userFromParam.setPassword(user.getPassword());
				
				
				userService.updateUser(userFromParam);
				
				return "redirect:/Admin/adminAccounts";
				
			}
			return "403"; 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "error/403";
	}*/
	
}
