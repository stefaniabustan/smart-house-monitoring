package ro.bstefania.ds2024.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class PersonRef  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Type(type="long")
    private Long id;

    @OneToMany(mappedBy = "personId" , fetch =FetchType.EAGER)
    private List<Device> devices;
    public PersonRef() {
    }
    public PersonRef(Long id) {
        this.id = id;
        this.devices= new ArrayList<>();
    }



    public Long getId() {
        return id;
    }


    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        List<Device> myDevs= devices;
        String listDevices ="";
        for(Device dev: myDevs)
            listDevices+= dev.getId()+", ";
        return "PersonRef{" +
                "id=" + id +
                ", devices=" + listDevices +
                '}';
    }
}
