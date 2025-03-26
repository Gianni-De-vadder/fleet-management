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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import fr.extia.mentoring.fleetmanagement.entities.LoadLevel;
import fr.extia.mentoring.fleetmanagement.entities.Tractor;
import fr.extia.mentoring.fleetmanagement.errors.NotFoundException;
import fr.extia.mentoring.fleetmanagement.repositories.TractorRepository;

class TractorServiceTest {
    TractorRepository tractorRepositoryMock = mock(TractorRepository.class);

    TractorService service = new TractorService(tractorRepositoryMock);

    @Test
    void testFindAll() {
        // Given
        List<Tractor> expected = tractors().toList();
        when(tractorRepositoryMock.findAll()).thenReturn(expected);

        // When
        List<Tractor> actual = service.findAll();

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        verify(tractorRepositoryMock, times(1)).findAll();
    }

    @Test
    void testFindByIdIsKo() {
        // Given
        when(tractorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class,
                () -> service.findById(1L));
    }

    @Test
    void testFindByIdIsOk() {
        // Given
        Tractor expected = tractors().findAny().get();
        when(tractorRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expected));

        // When
        Tractor actual = service.findById(1L);

        // Then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(LoadLevel.class)
    void testFindByPower(LoadLevel power) {
        // Given
        List<Tractor> expected = new ArrayList<>();
        ;

        when(tractorRepositoryMock.findByPower(any(LoadLevel.class)))
                .thenAnswer(call -> {
                    LoadLevel level = call.getArgument(0, LoadLevel.class);
                    tractors(level).forEach(expected::add);
                    return expected;
                });

        // When
        List<Tractor> actual = service.findByPower(power);

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testUpdateIsKoNoId() {
        // Given
        Tractor t = new Tractor();
        t.setId(null);
        t.setName("t");
        t.setPower(LoadLevel.LIGHT);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.update(t));
        verify(tractorRepositoryMock, times(0)).findById(anyLong());
        verify(tractorRepositoryMock, times(0)).save(any(Tractor.class));
    }

    @Test
    void testUpdateIsKoNoTractorInDatabase() {
        // Given
        Tractor t = new Tractor();
        t.setId(1L);
        t.setName("t1");
        t.setPower(LoadLevel.LIGHT);

        when(tractorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class,
                () -> service.update(t));
        verify(tractorRepositoryMock, times(1)).findById(anyLong());
        verify(tractorRepositoryMock, times(0)).save(any(Tractor.class));
    }

    @ParameterizedTest
    @MethodSource("updateParameters")
    void testUpdateIsOk(Tractor paramTractor, Tractor databaseTractor, Tractor expectedSavedTractor) {
        // Given
    	Tractor databaseCopy = new Tractor(databaseTractor);
    	
        when(tractorRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(databaseCopy));
        when(tractorRepositoryMock.save(any(Tractor.class)))
                .thenAnswer(call -> call.getArgument(0, Tractor.class));

        // When
        Tractor actualSavedTractor = service.update(paramTractor);

        // Then
        assertEquals(expectedSavedTractor, actualSavedTractor);
    }
    
    static Stream<Arguments> updateParameters(){
        var databaseTractor = new Tractor();
        
        databaseTractor.setId(1L);
        databaseTractor.setName("Renault Blanc");
        databaseTractor.setPower(LoadLevel.MEDIUM);
        
        
        var paramTractor1 = new Tractor();
        var paramTractor2 = new Tractor();
        var paramTractor3 = new Tractor();
        var paramTractor4 = new Tractor();
        
        var expectedSavedTractor1 = new Tractor();
        var expectedSavedTractor2 = new Tractor();
        var expectedSavedTractor3 = new Tractor();
        var expectedSavedTractor4 = new Tractor();
        
        paramTractor1.setId(1L);
        expectedSavedTractor1.setId(1L);
        expectedSavedTractor1.setName("Renault Blanc");
        expectedSavedTractor1.setPower(LoadLevel.MEDIUM);
        
        paramTractor2.setId(1L);
        paramTractor2.setPower(LoadLevel.LIGHT);
        expectedSavedTractor2.setId(1L);
        expectedSavedTractor2.setName("Renault Blanc");
        expectedSavedTractor2.setPower(LoadLevel.LIGHT);
        
        paramTractor3.setId(1L);
        paramTractor3.setName("Renault Rouge");
        expectedSavedTractor3.setId(1L);
        expectedSavedTractor3.setName("Renault Rouge");
        expectedSavedTractor3.setPower(LoadLevel.MEDIUM);
        
        paramTractor4.setId(1L);
        paramTractor4.setName("Renault Vert");
        paramTractor4.setPower(LoadLevel.HEAVY);
        expectedSavedTractor4.setId(1L);
        expectedSavedTractor4.setName("Renault Vert");
        expectedSavedTractor4.setPower(LoadLevel.HEAVY);
        
        
        return Stream.of(
                Arguments.of(paramTractor1, databaseTractor, expectedSavedTractor1),
                Arguments.of(paramTractor2, databaseTractor, expectedSavedTractor2),
                Arguments.of(paramTractor3, databaseTractor, expectedSavedTractor3),
                Arguments.of(paramTractor4, databaseTractor, expectedSavedTractor4) );
    }

    private static Stream<Tractor> tractors() {
        var t1 = new Tractor();
        var t2 = new Tractor();
        var t3 = new Tractor();
        var t4 = new Tractor();

        t1.setId(1L);
        t2.setId(2L);
        t3.setId(3L);
        t4.setId(4L);

        t1.setPower(LoadLevel.LIGHT);
        t2.setPower(LoadLevel.MEDIUM);
        t3.setPower(LoadLevel.MEDIUM);
        t4.setPower(LoadLevel.HEAVY);

        t1.setName("t1");
        t2.setName("t4");
        t3.setName("t4");
        t4.setName("t4");

        return Stream.of(t1, t2, t3, t4);
    }

    private static Stream<Tractor> tractors(LoadLevel power) {
        return tractors().filter(tractor -> tractor.getPower().equals(power));
    }

}
