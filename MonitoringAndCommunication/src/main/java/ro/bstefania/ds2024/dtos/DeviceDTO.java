package ro.bstefania.ds2024.dtos;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {

    private Long id;
    private Long idUser;
    private Long idDevice;
    private double mhc;

    public DeviceDTO() {
    }

    public DeviceDTO(Long id, Long idUser, Long idDevice, double mhc) {
        this.id = id;
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.mhc = mhc;
    }
    public DeviceDTO( Long idUser, Long idDevice, double mhc) {
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.mhc = mhc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(Long idDevice) {
        this.idDevice = idDevice;
    }

    public double getMhc() {
        return mhc;
    }

    public void setMhc(double mhc) {
        this.mhc = mhc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Double.compare(deviceDTO.mhc, mhc) == 0 && Objects.equals(id, deviceDTO.id) && Objects.equals(idUser, deviceDTO.idUser) && Objects.equals(idDevice, deviceDTO.idDevice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, idUser, idDevice, mhc);
    }
}
