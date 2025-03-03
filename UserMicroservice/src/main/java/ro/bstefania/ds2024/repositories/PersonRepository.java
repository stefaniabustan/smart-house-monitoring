package ro.bstefania.ds2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.bstefania.ds2024.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Example: JPA generate Query by Field
     */
    Optional<Person> findByUsername(String username);

}
