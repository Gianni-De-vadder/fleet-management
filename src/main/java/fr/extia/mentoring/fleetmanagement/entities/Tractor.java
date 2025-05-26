package fr.extia.mentoring.fleetmanagement.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "Tractors")
public class Tractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoadLevel power;
    
    public Tractor() {
    	
    }
    
    public Tractor(Tractor original) {
    	this.id = original.getId();
    	this.name=original.getName();
    	this.power=original.getPower();
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

    public LoadLevel getPower() {
        return power;
    }

    public void setPower(LoadLevel power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "Tractor [id=" + id + ", name=" + name + ", power=" + power + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, power);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tractor other = (Tractor) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name) && power == other.power;
    }
}
