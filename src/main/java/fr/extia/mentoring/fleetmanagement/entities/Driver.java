package fr.extia.mentoring.fleetmanagement.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

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
    private List<DangerType> authorizations = new ArrayList<>();

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
        if (authorizations == null) {
            authorizations = new ArrayList<>();
        }
        authorizations.add(authorization);
    }
}