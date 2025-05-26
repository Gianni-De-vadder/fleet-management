package fr.extia.mentoring.fleetmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.extia.mentoring.fleetmanagement.entities.LoadLevel;
import fr.extia.mentoring.fleetmanagement.entities.Tractor;

@Repository
public interface TractorRepository extends JpaRepository<Tractor, Long> {
    public List<Tractor> findByPower(LoadLevel power);
}
