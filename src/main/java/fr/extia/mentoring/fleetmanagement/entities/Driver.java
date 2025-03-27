package fr.extia.mentoring.fleetmanagement.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "Drivers")
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass=DangerType.class)
	private List<DangerType> authorizations;

	public Driver() {

	}

	public Driver(Driver original) {
		this.id = original.id;
		this.name = original.name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DangerType> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<DangerType> authorizations) {
		this.authorizations = authorizations;
	}
	
	public void addAuthorization(DangerType authorization) {
		if(null==authorizations) {
			authorizations = new ArrayList<>();
		}
		authorizations.add(authorization);
	}


}
