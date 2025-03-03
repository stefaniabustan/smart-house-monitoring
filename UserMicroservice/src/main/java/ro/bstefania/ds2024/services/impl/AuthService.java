package ro.bstefania.ds2024.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.bstefania.ds2024.dtos.PersonLoginDTO;
import ro.bstefania.ds2024.entities.Person;
import ro.bstefania.ds2024.repositories.PersonRepository;
import ro.bstefania.ds2024.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {
    private final PersonRepository personRepository;
   private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }



    public Map<String, Object> authenticate(PersonLoginDTO loginDTO) {
        Optional<Person> personOptional = personRepository.findByUsername(loginDTO.getUsername());

        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            System.out.println(person.toString());
            if (passwordEncoder.matches(loginDTO.getPassword(), person.getPassword())) {
                String jwt = jwtUtil.generateToken(person.getUsername(), person.getIsAdmin());
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("person", person); //return the full person object
                return response;
            }
        }

        throw new RuntimeException("Invalid username or password");
    }


    public boolean doesUserExist(String username) {
        return personRepository.findByUsername(username).isPresent();
    }}
