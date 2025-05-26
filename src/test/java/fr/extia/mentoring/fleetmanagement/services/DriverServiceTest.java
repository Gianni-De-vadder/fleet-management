package fr.extia.mentoring.fleetmanagement.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.extia.mentoring.fleetmanagement.entities.DangerType;
import fr.extia.mentoring.fleetmanagement.entities.Driver;
import fr.extia.mentoring.fleetmanagement.errors.NotFoundException;
import fr.extia.mentoring.fleetmanagement.repositories.DriverRepository;

class DriverServiceTest {
	DriverRepository driverRepositoryMock = mock(DriverRepository.class);

	DriverService service = new DriverService(driverRepositoryMock);

	@Test
	void testFindByIdIsOk() {
		// Given
		Driver expected = new Driver();
		expected.setId(1L);
		expected.setName("John");
		expected.addAuthorization(DangerType.FLAMMABLE);

		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expected));

		// When
		Driver actual = service.findById(1L);

		// Then
		assertEquals(expected, actual);
	}

	@Test
	void testFindByIdIsKo() {
		// Given
		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void testUpdateIsKoNoId() {
		// Given
		Driver d = new Driver();
		d.setId(null);
		d.setName("John");

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> service.update(d));
		verify(driverRepositoryMock, times(0)).findById(anyLong());
		verify(driverRepositoryMock, times(0)).save(any(Driver.class));
	}

	@Test
	void testUpdateIsKoDriverNotFound() {
		// Given
		Driver d = new Driver();
		d.setId(1L);
		d.setName("John");

		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundException.class, () -> service.update(d));
		verify(driverRepositoryMock, times(1)).findById(anyLong());
		verify(driverRepositoryMock, times(0)).save(any(Driver.class));
	}

	@ParameterizedTest
	@MethodSource("updateParameters")
	void testUpdateIsOk(Driver dbDriver, Driver paramDriver, Driver expected) {
		// Given
		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.of(dbDriver));
		when(driverRepositoryMock.save(any(Driver.class))).thenAnswer(call -> call.getArgument(0));

		// When
		Driver actual = service.update(paramDriver);

		// Then
		assertEquals(expected, actual);
	}

	static Stream<Arguments> updateParameters() {
		var dbDriver = new Driver();
		dbDriver.setId(1L);
		dbDriver.setName("Gianni");
		dbDriver.addAuthorization(DangerType.FLAMMABLE);

		var paramDriver1 = new Driver();
		var paramDriver2 = new Driver();
		var paramDriver3 = new Driver();

		var expected1 = new Driver();
		var expected2 = new Driver();
		var expected3 = new Driver();

		paramDriver1.setId(1L);
		expected1.setId(1L);
		expected1.setName("Gianni");

		paramDriver2.setId(1L);
		paramDriver2.setAuthorizations(Set.of(DangerType.FLAMMABLE, DangerType.RADIOACTIVE));
		expected2.setId(1L);
		expected2.setName("Gianni");
		expected2.setAuthorizations(Set.of(DangerType.RADIOACTIVE, DangerType.FLAMMABLE));

		paramDriver3.setId(1L);
		paramDriver3.setName("Antoine");
		expected3.setId(1L);
		expected3.setName("Antoine");

		return Stream.of(Arguments.of(dbDriver, paramDriver1, expected1),
				Arguments.of(dbDriver, paramDriver2, expected2), Arguments.of(dbDriver, paramDriver3, expected3)

		);
	}

	@ParameterizedTest
	@MethodSource("addAuthorizationsParameters")
	void testAddAuthorizations(Collection<DangerType> addedAuthorizations, Set<DangerType> expectedAuthorizations) {
		// GIVEN
		var dbDriver = new Driver();
		dbDriver.setId(1L);
		dbDriver.setName("Gianni");
		dbDriver.addAuthorization(DangerType.FLAMMABLE);

		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.of(dbDriver));
		when(driverRepositoryMock.save(any(Driver.class))).thenAnswer(call -> call.getArgument(0));

		// WHEN
		Set<DangerType> actualAuthorizations = service.addAuthorizations(1L, addedAuthorizations).getAuthorizations();

		// THEN
		assertThat(actualAuthorizations).containsExactlyInAnyOrderElementsOf(expectedAuthorizations);
	}

	static Stream<Arguments> addAuthorizationsParameters() {
		var c1 = Collections.emptyList();
		var s1 = Set.of(DangerType.FLAMMABLE);

		var c2 = List.of(DangerType.FLAMMABLE);
		var s2 = Set.of(DangerType.FLAMMABLE);

		var c3 = List.of(DangerType.CORROSIVE);
		var s3 = Set.of(DangerType.FLAMMABLE, DangerType.CORROSIVE);

		var c4 = List.of(DangerType.TOXIC, DangerType.TOXIC, DangerType.TOXIC);
		var s4 = Set.of(DangerType.FLAMMABLE, DangerType.TOXIC);

		var c5 = List.of(DangerType.EXPLOSIVE, DangerType.RADIOACTIVE, DangerType.CORROSIVE);
		var s5 = Set.of(DangerType.FLAMMABLE, DangerType.EXPLOSIVE, DangerType.RADIOACTIVE, DangerType.CORROSIVE);

		return Stream.of(Arguments.of(c1, s1), Arguments.of(c2, s2), Arguments.of(c3, s3), Arguments.of(c4, s4),
				Arguments.of(c5, s5));

	}

	@ParameterizedTest
	@MethodSource("removeAuthKoParameters")
	void testRemoveAuthorizationsKO(Set<DangerType> originalAuth, Collection<DangerType> removedAuth) {
		// Given
		Driver dbDriver = new Driver();
		dbDriver.setId(1L);
		dbDriver.setName("Gianni");
		dbDriver.setAuthorizations(originalAuth);

		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.of(dbDriver));

		// When & Then
		assertThrows(IllegalArgumentException.class, () -> service.removeAuthorizations(1L, removedAuth));

		verify(driverRepositoryMock, times(1)).findById(1L);
		verify(driverRepositoryMock, times(0)).save(any(Driver.class));
	}

	static Stream<Arguments> removeAuthKoParameters() {
		return Stream.of(Arguments.of(Collections.emptySet(), List.of(DangerType.FLAMMABLE)),
				Arguments.of(Set.of(DangerType.FLAMMABLE), List.of(DangerType.CORROSIVE)),
				Arguments.of(Set.of(DangerType.FLAMMABLE, DangerType.RADIOACTIVE), List.of(DangerType.CORROSIVE)),
				Arguments.of(Set.of(DangerType.FLAMMABLE, DangerType.CORROSIVE),
						List.of(DangerType.CORROSIVE, DangerType.CORROSIVE)));
	}

	@ParameterizedTest
	@MethodSource("removeAuthOkParameters")
	void testRemoveAuthorizationsOk(Set<DangerType> originalAuth, Collection<DangerType> removedAuth,
			Set<DangerType> expectedAuthorizations) {
		// GIVEN
		Driver dbDriver = new Driver();
		dbDriver.setId(1L);
		dbDriver.setName("Gianni");
		dbDriver.setAuthorizations(originalAuth);

		when(driverRepositoryMock.findById(anyLong())).thenReturn(Optional.of(dbDriver));
		when(driverRepositoryMock.save(any(Driver.class))).thenAnswer(call -> call.getArgument(0));

		// WHEN
		Set<DangerType> actualAuthorizations = service.removeAuthorizations(1L, removedAuth).getAuthorizations();

		// THEN
		assertThat(actualAuthorizations).containsExactlyInAnyOrderElementsOf(expectedAuthorizations);
	}

	static Stream<Arguments> removeAuthOkParameters() {
		return Stream.of(
				Arguments.of(Set.of(DangerType.FLAMMABLE), List.of(DangerType.FLAMMABLE), Collections.emptySet()),
				Arguments.of(Set.of(DangerType.FLAMMABLE, DangerType.CORROSIVE, DangerType.EXPLOSIVE), List.of(DangerType.FLAMMABLE), Set.of(DangerType.CORROSIVE, DangerType.EXPLOSIVE)),
				Arguments.of(Set.of(DangerType.FLAMMABLE, DangerType.CORROSIVE, DangerType.EXPLOSIVE), List.of(DangerType.FLAMMABLE, DangerType.CORROSIVE), Set.of(DangerType.EXPLOSIVE)),
				Arguments.of(Set.of(DangerType.FLAMMABLE, DangerType.CORROSIVE, DangerType.EXPLOSIVE), List.of(DangerType.FLAMMABLE, DangerType.CORROSIVE, DangerType.EXPLOSIVE), Collections.emptySet())
				);
	}
}
