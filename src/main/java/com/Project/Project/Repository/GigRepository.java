package com.Project.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Project.Project.Entity.Gig;

public interface GigRepository extends JpaRepository<Gig,Long>{

	@Query( value="SELECT * FROM services s WHERE s.titre like %:keyword%" , nativeQuery = true)
	List<Gig> findByKeyword(String keyword);
	
}
