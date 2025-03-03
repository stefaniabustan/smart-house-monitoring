package ro.bstefania.ds2024.dtos.builders;

import ro.bstefania.ds2024.dtos.PersonDTO;
import ro.bstefania.ds2024.dtos.PersonLoginDTO;
import ro.bstefania.ds2024.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO entityToPersonDTO(Person person) {
        return new PersonDTO(person.getId(), person.getUsername(), person.getPassword(), person.getIsAdmin());
    }

    public static Person personDTOtoEntity(PersonDTO personDTO) {
        return new Person(personDTO.getUsername(),
                personDTO.getPassword(),
                personDTO.getIsAdmin());
    }
    public static PersonLoginDTO entityToPersonLoginDTO(Person person) {
        return new PersonLoginDTO(person.getUsername(), person.getPassword());
    }

    public static Person personLoginDTOtoEntity(PersonLoginDTO personDTO) {
        return new Person(personDTO.getUsername(),
                personDTO.getPassword());
    }
}
