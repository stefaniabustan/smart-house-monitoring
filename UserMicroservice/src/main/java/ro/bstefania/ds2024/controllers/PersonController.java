package ro.bstefania.ds2024.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.bstefania.ds2024.dtos.PersonDTO;
import ro.bstefania.ds2024.services.interf.PersonService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonDTO>> getPersons() {

        List<PersonDTO> dtos = personService.findPersons();
        for (PersonDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/username")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> getUsernames() {

        List<PersonDTO> dtos = personService.findPersons();
        List<String> usernames = dtos.stream()
                .map(PersonDTO::getUsername)
                .collect(Collectors.toList());

        return new ResponseEntity<>(usernames, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") Long personId) {
        PersonDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> insertPerson(@Valid @RequestBody PersonDTO personDTO) {
        Long personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deletePerson(@PathVariable("id") Long personId) {
        Long personID= personService.delete(personId);
        return new ResponseEntity<>(personID, HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDTO personDTO) {
        try {
            // Call update method
            PersonDTO personUpdate = personService.update(personDTO.getId(), personDTO);
            return getResponseEntity(personUpdate);
        } catch (Exception e) {
            System.err.println("Error during person update: " + e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid request data"));
        }
    }


    static ResponseEntity<?> getResponseEntity(PersonDTO personUpdate) {
        if (personUpdate != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("person", personUpdate);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid email or password"));
    }
}
