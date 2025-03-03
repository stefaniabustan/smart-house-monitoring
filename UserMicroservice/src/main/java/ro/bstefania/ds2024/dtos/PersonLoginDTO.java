package ro.bstefania.ds2024.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class PersonLoginDTO extends RepresentationModel<PersonDTO> {
    private String username;
    private String password;

    public PersonLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonLoginDTO personDTO = (PersonLoginDTO) o;
        return Objects.equals(username, personDTO.username) && Objects.equals(password, personDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "PersonLoginDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
