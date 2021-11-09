package com.Project.Project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Gig;
import com.Project.Project.Repository.GigRepository;

@Service
public class GigService {

	@Autowired
	GigRepository gigRepo;
	
	//GET ALL GIGS (SERVICES)
	public List<Gig> getAllGigs(){
		return gigRepo.findAll();
	}
	
	//GET ONE GIG (SERVICE)
	public Gig getGigById(Long gigId) {
		return gigRepo.findById(gigId).get();
	}
	
	//NEW GIG
	public Gig newGig(Gig gig) {
		return gigRepo.save(gig);
	}
	
	//DELETE GIG
	public void deleteGig(Long gigId) {
		gigRepo.deleteById(gigId);
	}
	
	//UPDATE GIG
	public Gig updateGig(Gig gig) {
		return gigRepo.save(gig);
	}
	
	//ENABLE GIG
	
	//DISABLE GIG
	
	public List<Gig> getServicesByKeuword(String keyword){
		return gigRepo.findByKeyword(keyword);
	}
}
