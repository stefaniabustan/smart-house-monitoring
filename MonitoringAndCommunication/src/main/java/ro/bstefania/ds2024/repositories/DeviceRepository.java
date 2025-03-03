package ro.bstefania.ds2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.bstefania.ds2024.entities.Device;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Example: JPA generate Query by Field
     */
    Optional<Device> findByIdUserAndIdDevice(Long idUser, Long idDevice);
    Optional<Device> findByIdDevice(Long idDevice);


}