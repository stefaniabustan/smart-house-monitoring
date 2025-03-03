package ro.bstefania.ds2024.dtos;

import org.springframework.hateoas.RepresentationModel;
import ro.bstefania.ds2024.entities.Device;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class PersonRefDTO extends RepresentationModel<PersonRefDTO> {
    private Long id;
    private List<Device> devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public PersonRefDTO(Long id, List<Device> devices) {
        this.id = id;
        this.devices = new ArrayList<>();
    }
}
