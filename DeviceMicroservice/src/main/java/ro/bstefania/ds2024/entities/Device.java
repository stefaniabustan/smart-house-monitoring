package ro.bstefania.ds2024.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Device  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "mhc", nullable = false)
    private float mhc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="person_id")
    private PersonRef personId;

    public Device(){}

    public Device(Long id, String name, String description, String address, float mhc, Long personId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.mhc = mhc;
        this.personId = new PersonRef(personId);
    }

    public Device(Long id, String name, String description, String address, float mhc) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getMhc() {
        return mhc;
    }

    public void setMhc(float mhc) {
        this.mhc = mhc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonRef getPersonId() {
        return personId;
    }

    public void setPersonId(PersonRef personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", mhc=" + mhc +
                ", personId=" + ((personId == null) ? "device free" : personId.getId()) +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id); // Folosește id-ul sau alt atribut unic
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Folosește id-ul pentru hash
    }

}
