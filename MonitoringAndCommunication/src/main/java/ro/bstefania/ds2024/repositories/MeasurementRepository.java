package ro.bstefania.ds2024.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.bstefania.ds2024.entities.Measurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * Example: JPA generate Query by Field
     */
    Optional<Measurement> findByDeviceIdAndTimestamp(Long deviceId, LocalDateTime timestamp);

    @Query("SELECT m FROM Measurement m WHERE m.deviceId = :deviceId AND m.timestamp >= :startOfDay AND m.timestamp < :endOfDay")
    List<Measurement> findMeasurementsByDeviceIdAndDay(
            @Param("deviceId") Long deviceId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);


}