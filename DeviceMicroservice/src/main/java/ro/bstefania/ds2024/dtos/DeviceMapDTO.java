package ro.bstefania.ds2024.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class DeviceMapDTO extends RepresentationModel<DeviceMapDTO> {

    private Long id;

    private String name;

    private String description;

    private String address;

    private float mhc;
    private Long personId;

    private boolean map;


    DeviceMapDTO(){}


    public DeviceMapDTO(Long id, String name, String description, String address, float mhc, Long personId, boolean map) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.mhc = mhc;
        this.personId = personId;
        this.map = map;
    }

    public boolean getMap() {
        return map;
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


    public Long getPersonId() {
        return personId;
    }

    @Override
    public String toString() {
        return "DeviceMapDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", mhc=" + mhc +
                ", personId=" + personId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceMapDTO deviceDTO = (DeviceMapDTO) o;
        return Float.compare(deviceDTO.mhc, mhc) == 0 && Objects.equals(id, deviceDTO.id) && Objects.equals(name, deviceDTO.name) && Objects.equals(description, deviceDTO.description) && Objects.equals(address, deviceDTO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, address, mhc);
    }
}
