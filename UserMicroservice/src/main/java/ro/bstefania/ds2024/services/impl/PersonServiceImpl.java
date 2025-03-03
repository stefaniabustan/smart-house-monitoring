package ro.bstefania.ds2024.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ro.bstefania.ds2024.config.RabbitMQConfig;
import ro.bstefania.ds2024.dtos.PersonDTO;
import ro.bstefania.ds2024.dtos.builders.PersonBuilder;
import ro.bstefania.ds2024.entities.Person;
import ro.bstefania.ds2024.exception.ExternalServiceException;
import ro.bstefania.ds2024.repositories.PersonRepository;
import ro.bstefania.ds2024.services.interf.PersonService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;


    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, RestTemplate
                             restTemplate) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
    }


    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::entityToPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.entityToPersonDTO(personOptional.get());
    }

    public Long insert(PersonDTO personDTO) {
        Person person = PersonBuilder.personDTOtoEntity(personDTO);
        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());

        // Trimitere mesaj către RabbitMQ
        //rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, person);

        return person.getId();
    }


    public Long delete(Long personId) {
        Optional<Person> searchedEntity = personRepository.findById(personId);
        if (searchedEntity.isPresent()) {
            personRepository.delete(searchedEntity.get());
            LOGGER.debug("Person with id {} was deleted in db", personId);

            try {
                restTemplate.delete("http://device.localhost/device/byPerson/" + personId);
            } catch (HttpServerErrorException ex) {
                String responseBody = ex.getResponseBodyAsString();
                LOGGER.error("Failed to delete devices for person id {}: {}", personId, responseBody);
                throw new ExternalServiceException("Failed to delete devices for person id " + personId + " - Details: " + responseBody, ex);
            } catch (RestClientException ex) {
                LOGGER.error("Error occurred while calling external service for person id {}: {}", personId, ex.getMessage());
                throw new ExternalServiceException("External service error for person id " + personId, ex);
            }

        } else {
            LOGGER.error("Person with id {} not found", personId);
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }
        return personId;
    }


    public PersonDTO update(Long id, PersonDTO personDto) {
        Optional<Person> searchedEntity = personRepository.findById(id);
        if (searchedEntity.isPresent()) {
            Person updatedPerson = searchedEntity.get();
            updatedPerson.setUsername(personDto.getUsername());
            updatedPerson.setPassword(personDto.getPassword());
            updatedPerson.setISAdmin(personDto.getIsAdmin());
            updatedPerson = personRepository.save(updatedPerson);
            LOGGER.debug("Person with id {} was updated in db", updatedPerson.getId());
            return  PersonBuilder.entityToPersonDTO(updatedPerson);
        }
        return null;
    }

}
