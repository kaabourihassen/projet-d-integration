package com.Project.Project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Project.Project.Entity.ServicesRates;
import com.Project.Project.Repository.ServicesRatesRepository;

@Service
public class ServicesRatesService {

	@Autowired
	ServicesRatesRepository srRepo;
	
	
	public void newServicesRates(ServicesRates sr) {
		srRepo.save(sr);
	}
}
