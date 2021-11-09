package com.Project.Project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Request;
import com.Project.Project.Repository.RequestRepository;

@Service
public class RequestService {

	@Autowired
	RequestRepository reqRepo;
	
	//GET ALL REQUEST
	public List<Request> getAllRequests(){
		return reqRepo.findAll();
	}
	
	//GET ONE REQUEST
	public Request getRequestById(Long requestId) {
		return reqRepo.findById(requestId).get();
	}
	
	//NEW REQUEST
	public Request newRequest(Request request) {
		return reqRepo.save(request);
	}
	
	//DELETE REQUEST
	public void deleteRequest(Long requestId) {
		reqRepo.deleteById(requestId);
	}
	
	//UPDATE REQUEST
	public Request updateRequest(Request request) {
		return reqRepo.save(request);
	}
	//ENABLE REQUEST
	
	//DISABLE REQUEST
}
