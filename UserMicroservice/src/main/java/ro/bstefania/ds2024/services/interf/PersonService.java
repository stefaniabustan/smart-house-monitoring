package ro.bstefania.ds2024.services.interf;

import ro.bstefania.ds2024.dtos.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> findPersons();
    PersonDTO findPersonById(Long id);
    Long insert(PersonDTO personDTO);
    Long delete(Long personId);
    PersonDTO update(Long id, PersonDTO personDto);


}
