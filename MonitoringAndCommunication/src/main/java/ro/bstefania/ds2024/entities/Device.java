package ro.bstefania.ds2024.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Device  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "id_device", nullable = false)
    private Long idDevice;

    @Column(name = "mhc", nullable = false)
    private double mhc;

    public Device(Long id, Long idUser, Long idDevice, double mhc) {
        this.id = id;
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.mhc = mhc;
    }
    public Device() {}

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
}