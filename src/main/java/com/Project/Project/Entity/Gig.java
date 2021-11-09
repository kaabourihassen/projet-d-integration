package com.Project.Project.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.Project.Project.Entity.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Services")
public class Gig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String titre;
	@NotNull
	private String description;
	@NotNull
	private boolean enabled = true ;
	@NotNull
	private float prix;
	
	// Service Image relation
	@OneToOne(cascade = CascadeType.ALL ,mappedBy="gig")
	private Image image ;

	//SERVICE USER RELATION
	@ManyToOne
	@JoinColumn(name="coachId")
	private User user;
	
	//GIG SERVICES_RATES RELATION
	@OneToMany(mappedBy = "ratedService" , cascade = CascadeType.ALL)
	@JsonIgnore
	private Collection<ServicesRates> ratedServices = new ArrayList<>();
	
	public Gig() {

	}
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Gig(@NotNull String titre, @NotNull String description, @NotNull boolean enabled, @NotNull float prix,
			Image image, User user, Collection<ServicesRates> ratedServices) {
		super();
		this.titre = titre;
		this.description = description;
		this.enabled = enabled;
		this.prix = prix;
		this.image = image;
		this.user = user;
		this.ratedServices = ratedServices;
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<ServicesRates> getRatedServices() {
		return ratedServices;
	}

	public void setRatedServices(Collection<ServicesRates> ratedServices) {
		this.ratedServices = ratedServices;
	}

}
