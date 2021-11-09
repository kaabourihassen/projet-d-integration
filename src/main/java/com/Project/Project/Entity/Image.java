package com.Project.Project.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Images" )
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String path;
	
	//IMAGE SERVICE RELATION
	@OneToOne
	@JoinColumn(name="serviceId")
	private Gig gig;

	//IMAGE REQUEST RELATION
	@OneToOne
	@JoinColumn(name="requestId")
	private Request request;

	//IMAGE TEST RELATION
	@OneToOne
	@JoinColumn(name="testId")
	private Test test;
	
	//IMAGE USER RELATION
	@OneToOne
	//@JoinColumn(name="userId" , unique=true)
	@JoinColumn(name="userId" )
	private User user;
	
	public Image() {

	}

	public Image(Long id, String path) {
		super();
		this.id = id;
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Gig getGig() {
		return gig;
	}

	public void setGig(Gig gig) {
		this.gig = gig;
	}
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
}
