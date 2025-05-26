package fr.extia.mentoring.fleetmanagement.services;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.extia.mentoring.fleetmanagement.entities.DangerType;
import fr.extia.mentoring.fleetmanagement.entities.Driver;
import fr.extia.mentoring.fleetmanagement.errors.NotFoundException;
import fr.extia.mentoring.fleetmanagement.repositories.DriverRepository;

@Service
public class DriverService {

	private final DriverRepository driverRepository;

	public DriverService(DriverRepository driverRepository) {
		this.driverRepository = driverRepository;
	}

	public List<Driver> findAll() {
		return driverRepository.findAll();
	}

	public Driver findById(Long id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("No Driver found for ID: %s".formatted(id)));
	}

	public Driver create(Driver driver) {
		return driverRepository.save(driver);
	}

	public Driver update(Driver paramDriver) {
		if (paramDriver.getId() == null) {
			throw new IllegalArgumentException("No ID provided to update Driver: %s".formatted(paramDriver));
		}

		Driver existingDriver = driverRepository.findById(paramDriver.getId())
				.orElseThrow(() -> new NotFoundException("No Driver ID exists for Driver: %s".formatted(paramDriver)));

		if (StringUtils.isNotBlank(paramDriver.getName())) {
			existingDriver.setName(paramDriver.getName());
		}

		if (paramDriver.getAuthorizations() != null) {
			existingDriver.setAuthorizations(paramDriver.getAuthorizations());
		}

		return driverRepository.save(existingDriver);
	}

	public void delete(Long id) {
		driverRepository.deleteById(id);
	}

	public List<Driver> findByAuthorization(DangerType dangerType) {
		return driverRepository.findByAuthorizationsContaining(dangerType);
	}

	public Driver addAuthorizations(Long driverId, Collection<DangerType> authorizations) {
		Driver driver = findById(driverId);
		authorizations.forEach(driver.getAuthorizations()::add);
		return driverRepository.save(driver);
	}

	public Driver removeAuthorizations(Long driverId, Collection<DangerType> authorizations) {
		Driver driver = findById(driverId);

		for (DangerType auth : authorizations) {
			if (!driver.getAuthorizations().contains(auth)) {
				throw new IllegalArgumentException("Driver %s with id %s does not contain an authorization %s"
						.formatted(driver.getName(), driverId, auth));
			}

			driver.getAuthorizations().remove(auth);
		}

		return driverRepository.save(driver);
	}
}
