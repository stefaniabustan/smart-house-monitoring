package ro.bstefania.ds2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ro.bstefania.ds2024.entities.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Device> findByPersonId_Id(Long personId);


}
