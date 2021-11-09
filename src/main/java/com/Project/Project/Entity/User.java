package com.Project.Project.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import javax.persistence.JoinColumn;

@Entity
@Table(name="Users" , uniqueConstraints = @UniqueConstraint(columnNames= {"email","userName"}))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String userName;
	@NotNull
	private String nom;
	@NotNull
	private String prenom;
	@NotNull
	private String email;
	@NotNull
	private String phone;
	@NotNull
	private String adresse;
	@NotNull
	private String currentPost;
	@NotNull
	private boolean male;//hetha sexe mani7milch lenum . manich mot3asib wela faza hhh
	@NotNull
	private boolean enabled=true;
	@Column(length=64)
	@NotNull
	private String password;
	
	//USER ROLE RELATION
	@ManyToMany(cascade = CascadeType.ALL , fetch =FetchType.EAGER)
	@JoinTable(name="users_roles" , joinColumns = @JoinColumn(name="user_id") , inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>();
	
	//USER TEST RELATION demand d 'evaluation
	@ManyToMany(cascade = CascadeType.ALL , fetch =FetchType.EAGER)
	@JoinTable(name="evaluationDemands" , joinColumns = @JoinColumn(name="demander_id") , inverseJoinColumns=@JoinColumn(name="test_id"))
	private Set<Test> testDemands = new HashSet<>();
	
	//USER SERVICE RELATION
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="user")
	private List<Gig> services;
	
	//USER REQUEST RELATION
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="user")
	private List<Request> requests ;
	
	//USER IMAGE RELATION
	@OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="user")
	private Image image;
	
	//USER TEST RELATION
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="user")
	private List<Test> tests;
	
	//USER SERVICES_RATES RELATION
	@OneToMany(mappedBy = "rater" , cascade = CascadeType.ALL)
	private Collection<ServicesRates> servicesRated = new ArrayList<>();
	
	public User() {

	}
	

	
	public User(Long id, @NotNull String userName, @NotNull String nom, @NotNull String prenom, @NotNull String email,
			@NotNull boolean male, @NotNull boolean enabled, @NotNull String password, Set<Role> roles,
			Set<Test> testDemands, List<Gig> services, List<Request> requests, Image image, List<Test> tests,
			Collection<ServicesRates> servicesRated) {
		super();
		this.id = id;
		this.userName = userName;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.male = male;
		this.enabled = enabled;
		this.password = password;
		this.roles = roles;
		this.testDemands = testDemands;
		this.services = services;
		this.requests = requests;
		this.image = image;
		this.tests = tests;
		this.servicesRated = servicesRated;
	}



	public User(@NotNull String userName, @NotNull String nom, @NotNull String prenom, @NotNull String email,
			@NotNull boolean male, @NotNull boolean enabled, @NotNull String password, Set<Role> roles,
			Set<Test> testDemands, List<Gig> services, List<Request> requests, Image image, List<Test> tests,
			Collection<ServicesRates> servicesRated) {
		super();
		this.userName = userName;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.male = male;
		this.enabled = enabled;
		this.password = password;
		this.roles = roles;
		this.testDemands = testDemands;
		this.services = services;
		this.requests = requests;
		this.image = image;
		this.tests = tests;
		this.servicesRated = servicesRated;
	}



	public User(@NotNull String userName, @NotNull String nom, @NotNull String prenom, @NotNull String email,
			@NotNull boolean enabled, @NotNull String password, Set<Role> roles, Set<Test> testDemands,
			List<Gig> services, List<Request> requests, Image image, List<Test> tests,
			Collection<ServicesRates> servicesRated) {
		super();
		this.userName = userName;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.enabled = enabled;
		this.password = password;
		this.roles = roles;
		this.testDemands = testDemands;
		this.services = services;
		this.requests = requests;
		this.image= image;
		this.tests = tests;
		this.servicesRated = servicesRated;
	}



	public User(Long id, @NotNull String userName, @NotNull String nom, @NotNull String prenom, @NotNull String email,
			@NotNull String phone, @NotNull String adresse, @NotNull String currentPost, @NotNull boolean male,
			@NotNull boolean enabled, @NotNull String password, Set<Role> roles, Set<Test> testDemands,
			List<Gig> services, List<Request> requests, Image image, List<Test> tests,
			Collection<ServicesRates> servicesRated) {
		super();
		this.id = id;
		this.userName = userName;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.phone = phone;
		this.adresse = adresse;
		this.currentPost = currentPost;
		this.male = male;
		this.enabled = enabled;
		this.password = password;
		this.roles = roles;
		this.testDemands = testDemands;
		this.services = services;
		this.requests = requests;
		this.image = image;
		this.tests = tests;
		this.servicesRated = servicesRated;
	}



	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public List<Gig> getServices() {
		return services;
	}

	public void setServices(List<Gig> services) {
		this.services = services;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> defaultRole) {
		this.roles = defaultRole;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Test> getTestDemands() {
		return testDemands;
	}

	public void setTestDemands(Set<Test> testDemands) {
		this.testDemands = testDemands;
	}

	public Collection<ServicesRates> getServicesRated() {
		return servicesRated;
	}

	public void setServicesRated(Collection<ServicesRates> servicesRated) {
		this.servicesRated = servicesRated;
	}



	public boolean isMale() {
		return male;
	}



	public void setMale(boolean male) {
		this.male = male;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getAdresse() {
		return adresse;
	}



	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}



	public String getCurrentPost() {
		return currentPost;
	}



	public void setCurrentPost(String currentPost) {
		this.currentPost = currentPost;
	}
	
	
}
