package com.Project.Project.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Tests")
public class Test {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String titre;
	@NotNull
	private String description;
	@NotNull
	private float prix;
	@NotNull
	private boolean enabled = true ;
	
	

	//TEST USER RELATION DEMAND D'EVALUATION
	@ManyToMany(cascade = CascadeType.ALL , fetch =FetchType.LAZY , mappedBy = "testDemands" )
	private Set<User> users = new HashSet<>();
	
	//Test User Relation "owner" 
	@ManyToOne
	@JoinColumn(name="teacherId")
	@NotNull
	private User user;
	
	// TEST Image relation
	@OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="test")
	private Image image;

	public Test() {
		
	}

	public Test(String titre, String description, float prix, User user) {
		super();
		this.titre = titre;
		this.description = description;
		this.prix = prix;
		this.user = user;
	}
	
	public Test(@NotNull String titre, @NotNull String description, @NotNull float prix, @NotNull boolean enabled,
			Set<User> users, @NotNull User user, Image image) {
		super();
		this.titre = titre;
		this.description = description;
		this.prix = prix;
		this.enabled = enabled;
		this.users = users;
		this.user = user;
		this.image = image;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
