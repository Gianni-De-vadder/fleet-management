package fr.extia.mentoring.fleetmanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.extia.mentoring.fleetmanagement.entities.DangerType;
import fr.extia.mentoring.fleetmanagement.entities.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByAuthorizationsContaining(DangerType dangerType);
}
