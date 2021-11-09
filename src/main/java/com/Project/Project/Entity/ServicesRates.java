package com.Project.Project.Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class ServicesRates {
	
	@EmbeddedId
	private ServicesRatesId id = new ServicesRatesId();
	
	@ManyToOne
	@MapsId("id")
	@JoinColumn(name="raterId")
	@JsonIgnore
	private User rater;
	
	@ManyToOne
	@MapsId("id")
	@JoinColumn(name="serviceId")
	private Gig ratedService;
	
	private float nbreStars=0;

	public ServicesRatesId getId() {
		return id;
	}

	public void setId(ServicesRatesId id) {
		this.id = id;
	}

	public User getRater() {
		return rater;
	}

	public void setRater(User rater) {
		this.rater = rater;
	}

	public Gig getRatedService() {
		return ratedService;
	}

	public void setRatedService(Gig ratedService) {
		this.ratedService = ratedService;
	}

	public float getNbreStars() {
		return nbreStars;
	}

	public void setNbreStars(float nbreStars) {
		this.nbreStars = nbreStars;
	}

}
