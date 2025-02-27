package fr.extia.mentoring.fleetmanagement.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.extia.mentoring.fleetmanagement.entities.LoadLevel;
import fr.extia.mentoring.fleetmanagement.entities.Tractor;
import fr.extia.mentoring.fleetmanagement.errors.NotFoundException;
import fr.extia.mentoring.fleetmanagement.repositories.TractorRepository;

@Service
public class TractorService {

    private final TractorRepository tractorRepository;

    public TractorService(TractorRepository tractorRepository) {
        this.tractorRepository = tractorRepository;
    }

    public List<Tractor> findAll() {
        return tractorRepository.findAll();
    }

    public Tractor findById(Long id) {
        return tractorRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "No tractor found for ID: %S".formatted(id)));
    }
    
    public List<Tractor> findByPower(LoadLevel power){
        return tractorRepository.findByPower(power);
    }

}
