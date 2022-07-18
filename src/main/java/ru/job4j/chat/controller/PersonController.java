package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * PersonController rest api модели person.
 * 5. Обработка исключений и Spring REST [#504797]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final RestTemplate rest;
    private final PersonService persons;
    private static final String API_ROLE_ID = "http://localhost:8080/role/{id}";
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public PersonController(RestTemplate rest, PersonService persons,
                            BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.rest = rest;
        this.persons = persons;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public Iterable<Person> findAll(HttpServletRequest req) {
        LOG.info("Find all person");
        Iterable<Person> result = this.persons.findAll();
        for (Person person : result) {
            person.setRole(
                    this.rest.getForObject(API_ROLE_ID, Role.class, person.getRole().getId())
            );
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        LOG.info("Person find by id={}", id);
        Optional<Person> person = this.persons.findById(id);
        person.ifPresent(p -> p.setRole(
                rest.getForObject(
                        API_ROLE_ID,
                        Role.class,
                        p.getRole().getId()
                )));
        return new ResponseEntity<>(
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Person is not found. Please, check requisites."
                )),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        LOG.info("Save person={}", person);
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Invalid login or password");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. "
                            + "Password length must be more than 5 characters.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        LOG.info("Update person={}", person);
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Invalid login or password");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. "
                            + "Password length must be more than 5 characters.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        LOG.info("Encoding password={}", person.getPassword());
        this.persons.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        LOG.info("Delete person by id={}", id);
        Person person = new Person();
        person.setId(id);
        this.persons.delete(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOG.error(e.getLocalizedMessage());
    }
}
