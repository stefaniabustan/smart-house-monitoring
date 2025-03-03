package ro.bstefania.ds2024.dtos;

import org.springframework.hateoas.RepresentationModel;
import ro.bstefania.ds2024.entities.PersonRef;

import java.util.List;
import java.util.Objects;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {

    private Long id;

    private String name;

    private String description;

    private String address;

    private float mhc;



    DeviceDTO(){}

    public DeviceDTO(Long id, String name, String description, String address, float mhc) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.mhc = mhc;
    }

    public Long getId() {
        return id;
    }


    public String getDescription() {
        return description;
    }


    public String getAddress() {
        return address;
    }


    public float getMhc() {
        return mhc;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "DeviceDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", mhc=" + mhc +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Float.compare(deviceDTO.mhc, mhc) == 0 && Objects.equals(id, deviceDTO.id) && Objects.equals(name, deviceDTO.name) && Objects.equals(description, deviceDTO.description) && Objects.equals(address, deviceDTO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, address, mhc);
    }
}
