package fr.extia.mentoring.fleetmanagement.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.extia.mentoring.fleetmanagement.entities.LoadLevel;
import fr.extia.mentoring.fleetmanagement.entities.Tractor;
import fr.extia.mentoring.fleetmanagement.services.TractorService;

@RestController
@RequestMapping("tractors")
public class TractorController {
    private final TractorService tractorService;

    public TractorController(TractorService tractorService) {
        this.tractorService = tractorService;
    }

    @GetMapping
    public List<Tractor> findAll() {
        return tractorService.findAll();
    }

    @GetMapping("/{id}")
    public Tractor findById(@PathVariable Long id) {
        return tractorService.findById(id);
    }

    @GetMapping("/power/{power}")
    public List<Tractor> findByPower(@PathVariable String power) {
        LoadLevel enumPower = LoadLevel.fromValue(power);
        return tractorService.findByPower(enumPower);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tractor create(@RequestBody Tractor tractor) {
        return tractorService.create(tractor);
    }

    @PatchMapping
    public Tractor update(@RequestBody Tractor tractor) {
        return tractorService.update(tractor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tractorService.delete(id);
    }
}
