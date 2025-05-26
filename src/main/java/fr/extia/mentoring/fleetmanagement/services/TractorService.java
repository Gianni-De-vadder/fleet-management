package fr.extia.mentoring.fleetmanagement.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

    public List<Tractor> findByPower(LoadLevel power) {
        return tractorRepository.findByPower(power);
    }

    public Tractor create(Tractor tractor) {
        return tractorRepository.save(tractor);
    }

    public Tractor update(Tractor paramTractor) {
        if (null == paramTractor.getId()) {
            throw new IllegalArgumentException("No ID provided to update tractor: %s".formatted(paramTractor));
        }

        Tractor existingTractor = tractorRepository
                .findById(paramTractor.getId())
                .orElseThrow(() -> new NotFoundException("No tractor ID exists for tractor: %s".formatted(paramTractor)));

        if (StringUtils.isNotBlank(paramTractor.getName())) {
            existingTractor.setName(paramTractor.getName());
        }

        if (null != paramTractor.getPower()) {
            existingTractor.setPower(paramTractor.getPower());
        }

        return tractorRepository.save(existingTractor);
    }
    
    public void delete(Long id) {
        tractorRepository.deleteById(id);
    }

}
