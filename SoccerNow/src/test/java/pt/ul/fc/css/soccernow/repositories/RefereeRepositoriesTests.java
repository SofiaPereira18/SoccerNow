package pt.ul.fc.css.soccernow.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.repositories.RefereeRepository;

/**
 * Tests for the referee repository.
 * 
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
@DataJpaTest
public class RefereeRepositoriesTests {

    @Autowired 
    private RefereeRepository refereeRepository;

    private final List<Long> ids = new ArrayList<>();

    /**
     * Sets up initial data for testing, saving referees to the repository.
     */
    @BeforeEach
    void setup() {
        ids.add(refereeRepository.save(new Referee("Tomas", "Silva", true, null)).getId());
        ids.add(refereeRepository.save(new Referee("Jonas", "Santos", true, null)).getId());
        ids.add(refereeRepository.save(new Referee("Andre", "Santos", false, null)).getId());
    }

    /**
     * Tests saving a new referee.
     * Verifies that the referee is saved correctly and has a valid ID.
     */
    @Test
    void shouldSaveReferee() {
        Referee newReferee = new Referee("Tiago", "Nogueira", true, null);
        Referee savedReferee = refereeRepository.save(newReferee);
        assertNotNull(savedReferee.getId());
        assertEquals("Tiago", savedReferee.getFirstName());
        assertEquals("Nogueira", savedReferee.getLastName());
    }

    /**
     * Tests finding a referee by its ID.
     * Verifies that the correct referee is found by the provided ID.
     */
    @Test
    void shouldFindRefereeById() {
        Optional<Referee> referee = refereeRepository.findById(ids.get(1));
        assertTrue(referee.isPresent());
    }

    /**
     * Tests finding all referees in the repository.
     * Verifies that the correct number of referees are returned.
     */
    @Test
    void shouldFindAllReferees() {
        List<Referee> allReferees = refereeRepository.findAll();
        assertTrue(allReferees.size() == 3);
    }

    /**
     * Tests deleting a referee by its ID.
     * Verifies that the referee is successfully deleted from the repository.
     */
    @Test
    void shouldDeleteReferee() {
        Long idToDelete = ids.get(0);
        refereeRepository.deleteById(idToDelete);
        Optional<Referee> deletedReferee = refereeRepository.findById(idToDelete);
        assertTrue(deletedReferee.isEmpty());
    }

    /**
     * Tests updating a referee's information.
     * Verifies that the referee's information is updated correctly.
     */
    @Test
    void shouldUpdateReferee() {
        Referee refereeToUpdate = refereeRepository.findById(ids.get(0)).get();
        refereeToUpdate.setFirstName("UpdatedName");
        Referee updatedReferee = refereeRepository.save(refereeToUpdate);
        assertEquals("UpdatedName", updatedReferee.getFirstName());
    }

    /**
     * Tests finding referees by their certification status.
     * Verifies that referees with the specified certification status are returned.
     */
    @Test
    void shouldFindCertifiedReferees() {
        List<Referee> certifiedReferees = refereeRepository.findByHasCertificate(true);
        assertTrue(certifiedReferees.size() == 2);
    }

    /**
     * Tests finding referees who are not certified.
     * Verifies that the correct referees are returned based on their certification status.
     */
    @Test
    void shouldFindNonCertifiedReferees() {
        List<Referee> nonCertifiedReferees = refereeRepository.findByHasCertificate(false);
        assertTrue(nonCertifiedReferees.size() == 1);
    }

    /**
     * Tests finding a referee by first name.
     * Verifies that the correct referee is returned by the given first name.
     */
    @Test
    void shouldFindRefereeByFirstName() {
        List<Referee> referees = refereeRepository.findByFirstName("Tomas");
        assertTrue(referees.size() == 1);
    }

    /**
     * Tests finding a referee by both first and last name.
     * Verifies that the correct referee is returned by the combination of first and last name.
     */
    @Test
    void shouldFindRefereeByFirstAndLastName() {
        List<Referee> referees = refereeRepository.findByFirstNameAndLastName("Tomas", "Silva");
        assertTrue(referees.stream()
            .anyMatch(referee -> referee.getFirstName().equals("Tomas") 
            && referee.getLastName().equals("Silva")));
    }
}
