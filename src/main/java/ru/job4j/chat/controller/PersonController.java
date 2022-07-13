package ru.job4j.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.PersonService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * PersonController rest api модели person.
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

    public PersonController(RestTemplate rest, PersonService persons) {
        this.rest = rest;
        this.persons = persons;
    }

    @GetMapping("/")
    public Iterable<Person> findAll() {
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
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        LOG.info("Save person={}", person);
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        LOG.info("Update person={}", person);
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
}
