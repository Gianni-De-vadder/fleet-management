package fr.extia.mentoring.fleetmanagement.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity(name = "Drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection(targetClass = DangerType.class)
    @CollectionTable(name = "driver_authorizations", joinColumns = @JoinColumn(name = "driver_id"))
    @Enumerated(EnumType.STRING)
    private Set<DangerType> authorizations = new HashSet<>();

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

    public Set<DangerType> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<DangerType> authorizations) {
        this.authorizations = authorizations;
    }

    public void addAuthorization(DangerType authorization) {
        if (authorizations == null) {
            authorizations = new HashSet<>();
        }
        authorizations.add(authorization);
    }
    public void removeAuthorization(DangerType authorization) {
        if (authorizations != null) {
            authorizations.remove(authorization);
        }
    }

}