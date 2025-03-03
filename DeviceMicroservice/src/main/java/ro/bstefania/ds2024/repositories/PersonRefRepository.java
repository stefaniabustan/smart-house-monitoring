package ro.bstefania.ds2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.bstefania.ds2024.entities.Device;
import ro.bstefania.ds2024.entities.PersonRef;

public interface PersonRefRepository extends JpaRepository<PersonRef, Long> {
}
