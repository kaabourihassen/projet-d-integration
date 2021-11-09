package com.Project.Project.Controller;

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
import com.Project.Project.Entity.User;
import com.Project.Project.Service.GigService;
import com.Project.Project.Service.ImageService;
import com.Project.Project.Service.RoleService;
import com.Project.Project.Service.UserService;

@Controller
public class CoachController {

	@Autowired
	UserService userService;
	
	@Autowired
	GigService gigService;
	
	@Autowired
	ImageService imgService;
	
	@Autowired
	RoleService roleService; 
	
	//My Services
	@RequestMapping("Coach/myServices")
	public String getMyServices(Model model) throws Exception {
		try {
			User user=userService.getLoggedUser();
			
			List<Gig> enabledServices = new ArrayList<>();
			List<Gig> disabledServices = new ArrayList<>();
			
			for(Gig service : user.getServices()) {
				if(service.isEnabled()) {
					enabledServices.add(service);
				}else {
					disabledServices.add(service);
				}
			}
			
			model.addAttribute("enabledServices", enabledServices);
			model.addAttribute("disabledServices", disabledServices);
			
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return "Coach/myServices";
	}

	//ADD NEW SERVICE
	//GETTING NEW SERVICE FORM
	@RequestMapping("Coach/newService")
	public String getNewServiceForm( Model model) throws Exception {
		
		try {
			Gig service=new Gig();
			User userData=userService.getLoggedUser();

			service.setUser(userData);
			
			model.addAttribute("service", service);
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "Coach/newService";
	}
	
	//ADD NEW SERVICE TO THE DB
	@RequestMapping(value="Coach/addService" , method=RequestMethod.POST)
	public String addNewService(@ModelAttribute("service") Gig gig, @RequestParam("file") MultipartFile file,Model model)  throws Exception{
		try {
			Image img=new Image();
			User userData=userService.getLoggedUser();
			
			Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\servicesImages\\",file.getOriginalFilename());
			//System.out.println("die : "+pathToFile.toAbsolutePath());
			
			gig.setUser(userData);
			Gig G =gigService.newGig(gig);
			
			if(file.getOriginalFilename().isEmpty()) {
				img.setPath("/images/ServicesImages/thumbnail.jpg");//default image
			}else {
				img.setPath("/images/ServicesImages/"+file.getOriginalFilename());	
				Files.write(pathToFile, file.getBytes());
			}
			
			
			
			img.setGig(gig);
			imgService.newImage(img);

			model.addAttribute("gigs", G);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return "redirect:/Coach/myServices";
	}
	
	//DELETE SERVICE
	@RequestMapping("Coach/deleteService/{gigId}")
	public String deleteService(@PathVariable(name="gigId")Long gigId , RedirectAttributes redirectAttributes) throws Exception {
		try {
			User user=userService.getLoggedUser();//current loged in user
			Gig gig = gigService.getGigById(gigId);// service ili bch titfase5
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if( user.getRoles().contains(adminRole) || gig.getUser().getId() == user.getId()) {//if this service is owned by the current user
				if(gig.getImage() != null) {//itha ken 3andha taswira kawa9tha yfase5ha wba3ed yet3adda yfasse5 request (Contraint d'integriter )
					
						imgService.deleteImage(gig.getImage().getId());	
					
				}
				
				//It looks like hibernate is deleting the "services_rates" related lines by his own so we r ok I guess. 
				
				gigService.deleteGig(gigId);
				
				try {
					if(gigService.getGigById(gigId) != null ) {
						
						redirectAttributes.addFlashAttribute("Failed" , "Failed to delete test : " + gig.getTitre());
					}
					
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly deleted test : " + gig.getTitre());
				}
				
				if( gig.getUser().getId() == user.getId()) {
					return "redirect:/Coach/myServices";
					
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/services/adminServices";
					}
				}
				
				return "redirect:/Coach/myServices";
			}
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "error/403";
	}
	
	
	//UPDATE Service
	//Get edit service form
	@RequestMapping("Coach/editService/{gigId}")
	public String getEditServiceForm(@PathVariable(name="gigId")Long gigId , Model model) throws Exception {
		try {
			Gig gig=gigService.getGigById(gigId);
			User user=userService.getLoggedUser();
			
			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if( user.getRoles().contains(adminRole) || gig.getUser().getId() == user.getId()) {//if this service is owned by the current user
				model.addAttribute("gig", gig);
				return "Coach/editService";	
			}
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		return "error/403";//U Have no acces !(service mich mte3ou wela t7ayil mil url ,..)*/
		
	}
	
	//Update the service 
	@RequestMapping(value="Coach/updateService/{gigId}" , method= RequestMethod.POST)
	public String updateService(@PathVariable(name="gigId") Long gigId , @ModelAttribute(name="gig") Gig gig , @RequestParam("file") MultipartFile file , Model model , RedirectAttributes redirectAttributes) throws Exception {
		
		try {
			Image img=new Image();
			Gig G = gigService.getGigById(gigId); 			
			
			img.setId(G.getImage().getId());
			gig.setId(gigId);
			User user = userService.getLoggedUser();//getting all user info before updating the request

			Role adminRole = roleService.getRoleByName("ADMIN");
			
			if(user.getRoles().contains(adminRole) || user.getId() == G.getUser().getId()) {
				
				if(user.getRoles().contains(adminRole)) {
					gig.setUser(G.getUser());
				}else {
					gig.setUser(user);
				}
				
				Path pathToFile = Paths.get("src\\main\\resources\\static\\images\\servicesImages\\",file.getOriginalFilename());
				
				if(file.getOriginalFilename().isEmpty()) {
					gig.setImage(G.getImage());
					img.setPath("/images/ServicesImages/thumbnail.jpg");//default image
				}else {
					img.setGig(gig);
					img.setPath("/images/ServicesImages/"+file.getOriginalFilename());	
					Files.write(pathToFile, file.getBytes());
					imgService.updateImage(img);
				}
				
				
				Gig updated = null;
				updated = gigService.updateGig(gig);//updating service
				
				if(updated.getId() == null ) {//means failed to delete the test
					
					redirectAttributes.addFlashAttribute("Failed" , "Failed to update service : " + G.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly updated service : " + G.getTitre());
				}
				
				
				if(user.getId() == G.getUser().getId()) {
					return "redirect:/Coach/myServices";
				}else {
					if(user.getRoles().contains(adminRole)) {
						return "redirect:/services/"+"adminServices";
					}
				}
				
				return "redirect:/Coach/myServices";
			}
			
		}catch(Exception e) {
			System.out.println("die :"+e.getMessage());
		}
		
		return "error/403";
	}
	
	//ENABLE SERVICE
	@RequestMapping("/Coach/enableService/{serviceId}")
	public String enableService(@PathVariable(name="serviceId") Long serviceId , RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Gig gig = gigService.getGigById(serviceId);
			
			if(user.getRoles().contains(adminRole) || user.getId() == gig.getUser().getId()) {
				gig.setEnabled(true);
				gigService.updateGig(gig);
				
				if(gigService.getGigById(serviceId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly enabled service : " + gig.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to enable service : " + gig.getTitre());
				}
				
			}
			
			if(user.getId() == gig.getUser().getId()) {
				return "redirect:/Coach/myServices";
			}else {
				if(user.getRoles().contains(adminRole)) {
					return "redirect:/services/adminServices";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/Coach/myServices";
	}
	
	//DiSABLE SERVICE
	@RequestMapping("/Coach/disableService/{serviceId}")
	public String disableService(@PathVariable(name="serviceId") Long serviceId , RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getLoggedUser();
			Role adminRole = roleService.getRoleByName("ADMIN");
			Gig gig = gigService.getGigById(serviceId);
			
			if(user.getRoles().contains(adminRole) || user.getId() == gig.getUser().getId()) {
				gig.setEnabled(false);
				gigService.updateGig(gig);
				
				if(!gigService.getGigById(serviceId).isEnabled()) {
					redirectAttributes.addFlashAttribute("Succes" , "Succesfuly disabled service : " + gig.getTitre());
				}else {
					redirectAttributes.addFlashAttribute("Failed" , "Failed to disabel service : " + gig.getTitre());
				}
				
				
			}
			
			if(user.getId() == gig.getUser().getId()) {
				return "redirect:/Coach/myServices";
			}else {
				if(user.getRoles().contains(adminRole)) {
					return "redirect:/services/adminServices";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/Coach/myServices";
	}
}
