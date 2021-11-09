package com.Project.Project.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@Entity
@Table(name="Requests")
public class Request {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String titre;
	@NotNull
	private String description;
	@NotNull
	private String deadLine;// before 11 sept for example;
	
	@NotNull
	private boolean enabled = true ;
	
	// Request Image relation
	@OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy="request")
	private Image image;

	//Request User Relation
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	public Request() {

	}
	public Request(@NotNull String titre, @NotNull String description, @NotNull String deadLine,
			@NotNull boolean enabled, Image image, User user) {
		super();
		this.titre = titre;
		this.description = description;
		this.deadLine = deadLine;
		this.enabled = enabled;
		this.image = image;
		this.user = user;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
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
	@Override
	public String toString() {
		return "Request [id=" + id + ", titre=" + titre + ", description=" + description + ", deadLine=" + deadLine
				+ ", enabled=" + enabled + ", image=" + image + ", user=" + user + "]";
	}

}
