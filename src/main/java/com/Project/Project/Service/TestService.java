package com.Project.Project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.Test;
import com.Project.Project.Repository.TestRepository;

@Service
public class TestService {
	
	@Autowired
	TestRepository testRepo;
	
	//GET ALL TESTS
	public List<Test> getAllTests(){
		
		return testRepo.findAll();
	}
	
	//GET TEST BY ID
	public Test getTestById(Long id) {
		return testRepo.findById(id).get();
	}
	
	//  NEW TEST
	public Test addTest(Test test) {
		
		return testRepo.save(test);
	}
	
	// UPDATE TEST
	public Test updateTest(Test test) {
		return testRepo.save(test);
	}
	
	//DELETE TEST BY ID
	public void deleteTestById(Long id) {
		testRepo.deleteById(id);
	}
	
	//DELETE TEST BY ENTITY
	public void deleteTest(Test test) {
		testRepo.delete(test);
	}
}