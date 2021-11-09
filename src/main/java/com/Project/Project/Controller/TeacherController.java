package com.Project.Project.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import com.Project.Project.Entity.Role;
import com.Project.Project.Entity.Test;
import com.Project.Project.Entity.User;
import com.Project.Project.Service.ImageService;
import com.Project.Project.Service.RoleService;
import com.Project.Project.Service.TestService;
import com.Project.Project.Service.UserService;

@Controller
public class TeacherController {

	@Autowired
	UserService userService;
	
	@Autowired
	TestService testService;
	
	@Autowired
	ImageService imgService;
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping("/Teacher/myTests")
	public String getMyTests(Model model) {
		
		try {
			User user=userService.getLoggedUser();
			
			List<Test> enabledTests = new ArrayList<>() ;
			List<Test> disabledTests = new ArrayList<>() ;
			
			for(Test test : user.getTests()) {
				if(test.isEnabled()) {
					enabledTests.add(test);
				}else {
					disabledTests.add(test);
				}
			}
			
			model.addAttribute("enabledTests", enabledTests);
			model.addAttribute("disabledTests", disabledTests);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "/Teacher/myTests";
	}
	
	
	@RequestMapping("/Teacher/newTest")
	public String getNewTestForm(Model model) {
		
		try {
			Test test = new Test();
			User userData=userService.getLoggedUser();
			
			test.setUser(userData);
			
			model.addAttribute("test", test);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return "Teacher/newTest";
	}
	
	@RequestMapping(value="/Teacher/addTest" , method=RequestMethod.POST)
	public String addTest(@ModelAttribute("test") Test test, @RequestParam("file") MultipartFile file,Model model) {
		
		try {
			Image img=new Image();
			User userData=userService.getLoggedUser();
			
			Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\testsImages\\",file.getOriginalFilename());
			
			test.setUser(userData);
			
			Test t = testService.addTest(test);
			
			if(file.getOriginalFilename().isEmpty()) {
				img.setPath("/images/testsImages/thumbnail.jpg");//default image
			}else {
				img.setPath("/images/testsImages/"+file.getOriginalFilename());	
				Files.write(pathToFile, file.getBytes());
			}

			img.setTest(t);
			imgService.newImage(img);
			
			model.addAttribute("tests", userData.getTests());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/Teacher/myTests";
	}
	
	//Enable Test
	@RequestMapping("/Teacher/enableTest/{testId}")
	public String enableTest(@PathVariable(name="testId") Long testId , RedirectAttributes redirectAttributes) {
		
		try {
			User user=userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Test test = testService.getTestById(testId);
			
			if(user.getRoles().contains(adminRole) || user.getId() == test.getUser().getId()) {
				test.setEnabled(true);
				testService.updateTest(test);
				
				if(testService.getTestById(testId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly enabled test : " + test.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to enable test : " + test.getTitre());
				}
				
			}
			
			if(user.getId() == test.getUser().getId()) {
				return "redirect:/Teacher/myTests";
			}else {
				if(user.getRoles().contains(adminRole) ) {
					return "redirect:/tests/adminTests";
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/Teacher/myTests";
	}
	
	//Disable Test
	@RequestMapping("/Teacher/disableTest/{testId}")
	public String disableTest(@PathVariable(name="testId") Long testId , RedirectAttributes redirectAttributes) {
		
		try {
			User user=userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Test test = testService.getTestById(testId);
			

			if(user.getRoles().contains(adminRole) || user.getId() == test.getUser().getId()) {
				test.setEnabled(false);
				testService.updateTest(test);
				
				if(!testService.getTestById(testId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly disabled test : " + test.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to disable test : " + test.getTitre());
				}
			}
			
			if(user.getId() == test.getUser().getId()) {
				return "redirect:/Teacher/myTests";
			}else {
				if(user.getRoles().contains(adminRole) ) {
					return "redirect:/tests/adminTests";
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "redirect:/Teacher/myTests";
	}
	
	//Edit Test
	@RequestMapping("/Teacher/editTest/{testId}")
	public String getEditTestForm(@PathVariable(name="testId") Long testId , Model model) {
		
		try {
			User user=userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			Test t = testService.getTestById(testId);
			
			if(user.getRoles().contains(adminRole) || t.getUser().getId() == user.getId() ) {//if this test is owned by the current loged in user or he has "ADMIN" role 
				model.addAttribute("test", t);
				return "Teacher/editTest";		
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "403";
	}
	
	@RequestMapping(value="/Teacher/updateTest/{testId}", method=RequestMethod.POST )
	public String updateTest(@PathVariable(name="testId") Long testId , @ModelAttribute("test") Test test ,@RequestParam("file") MultipartFile file , RedirectAttributes redirectAttributes) {
		
		try {
			User user = userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			Test t = testService.getTestById(testId);
			
			if(user.getRoles().contains(adminRole) || user.getId() == t.getUser().getId()) {
				
				t.setTitre(test.getTitre());
				t.setDescription(test.getDescription());
				t.setPrix(test.getPrix());
				
				Image img=imgService.getImageById(t.getImage().getId());
				
				if(user.getRoles().contains(adminRole)) {
					test.setUser(t.getUser());
				}else {
					test.setUser(user);
				}
				
				Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\testsImages\\",file.getOriginalFilename());
				
				
				if(file.getOriginalFilename().isEmpty()) {
					//img.setPath("/images/testsImages/thumbnail.jpg");//default image
				}else {
					img.setPath("/images/testsImages/"+file.getOriginalFilename());	
					try {
						Files.write(pathToFile, file.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				img.setTest(t);
				imgService.updateImage(img);
				
				t.setImage(img);
				
				Test updated = null;
				updated = testService.updateTest(t);
				
				
				if(updated.getId() == null ) {//means failed to delete the test
					
					redirectAttributes.addFlashAttribute("Failed" , "Failed to update test : " + t.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly updated test : " + t.getTitre());
				}
				
				if(user.getId() == t.getUser().getId()) {
					return "redirect:/Teacher/myTests";
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/tests/"+"adminTests";
					}
				}
				
				return "redirect:/Teacher/myTests";
			}

			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//getting all user info before updating the request
		
		return "error/403";
	}
	
	//Delete Test
	@RequestMapping("/Teacher/deleteTest/{testId}")
	public String deleteTest(@PathVariable(name="testId") Long testId , Model model , RedirectAttributes redirectAttributes) {
		
		try {
			Test t = testService.getTestById(testId);
			User user=userService.getLoggedUser();//current loged in user
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if( user.getRoles().contains(adminRole) || t.getUser().getId() == user.getId()) {//itha ken lcurrent user is the owner of yhis test
				
				if(t.getImage() != null ) {
					
					imgService.deleteImage(t.getImage().getId());
					
				}
				
				if(t.getUsers() != null) {
					
					for(User demander : t.getUsers()) {
						demander.setTestDemands(null);
					}
					
					t.setUsers(null);
				}
				
				testService.deleteTest(t);
				
				try {
					if(testService.getTestById(testId) != null ) {//means failed to delete the test
						
						redirectAttributes.addFlashAttribute("Failed" , "Failed to delete test : " + t.getTitre());
					}
					
					
				}catch(Exception e) {// if the test is deleted the instruction 'if' will throw exception "no value present" ==> wich means that we Succesfuly deleted test from the DB.
					System.out.println(e.getMessage());
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly deleted test : " + t.getTitre());
				}
				
				if( t.getUser().getId() == user.getId()) {
					return "redirect:/Teacher/myTests";
					
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/tests/"+"adminTests";
					}
				}
				
				return "redirect:/Teacher/myTests";
				
			}else {
				return "error/403";
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "error/403";
	}
	
}
