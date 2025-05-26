package fr.extia.mentoring.fleetmanagement.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import fr.extia.mentoring.fleetmanagement.entities.Driver;
import fr.extia.mentoring.fleetmanagement.entities.DangerType;
import fr.extia.mentoring.fleetmanagement.services.DriverService;

@RestController
@RequestMapping("drivers")
public class DriverController {
	private final DriverService driverService;

	public DriverController(DriverService driverService) {
		this.driverService = driverService;
	}

	@GetMapping
	public List<Driver> findAll() {
		return driverService.findAll();
	}

	@GetMapping("/{id}")
	public Driver findById(@PathVariable Long id) {
		return driverService.findById(id);
	}

	@GetMapping("/authorization/{type}")
	public List<Driver> findByAuthorization(@PathVariable String type) {
		DangerType dangerType = DangerType.fromValue(type);
		return driverService.findByAuthorization(dangerType);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Driver create(@RequestBody Driver driver) {
		return driverService.create(driver);
	}

	@PatchMapping
	public Driver update(@RequestBody Driver driver) {
		return driverService.update(driver);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		driverService.delete(id);
	}

	@PatchMapping("/{driverId}/authorizations")
	public Driver addAuthorizations(@PathVariable Long driverId, @RequestBody List<String> strAuthorizations) {
		List<DangerType> authorizations = strAuthorizations
				.stream()
				.map(DangerType::fromValue)
				.toList();
		
		return driverService.addAuthorizations(driverId, authorizations);
	}
	@DeleteMapping("/{driverId}/authorizations")
	public Driver removeAuthorizations(@PathVariable Long driverId, @RequestBody List<String> strAuthorizations) {
		List<DangerType> authorizations = strAuthorizations
				.stream()
				.map(DangerType::fromValue)
				.toList();

		return driverService.removeAuthorizations(driverId, authorizations);
	}

}
