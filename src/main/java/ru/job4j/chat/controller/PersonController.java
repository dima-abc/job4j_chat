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
import ru.job4j.chat.domain.PersonDTO;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private final RoleService roles;

    public PersonController(RestTemplate rest, PersonService persons,
                            BCryptPasswordEncoder encoder, ObjectMapper objectMapper,
                            RoleService roles) {
        this.rest = rest;
        this.persons = persons;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
        this.roles = roles;
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

    /**
     * Создание новой записи через PersonDTO.
     *
     * @param personDTO DTO
     * @return ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody PersonDTO personDTO) {
        LOG.info("Save person={}", personDTO);
        Role role = roles.findById(personDTO.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (personDTO.getLogin() == null || personDTO.getPassword() == null) {
            throw new NullPointerException("Invalid login or password");
        }
        if (personDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. "
                            + "Password length must be more than 5 characters.");
        }
        personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        Person person = Person.of(personDTO.getLogin(), personDTO.getPassword(), role);
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновление заполненных полей модели через DTO.
     *
     * @param personDTO PersonDTO
     * @return ResponseEntity
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    @PatchMapping("/")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDTO) throws InvocationTargetException, IllegalAccessException {
        Optional<Person> current = persons.findById(personDTO.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (personDTO.getPassword() != null && personDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. "
                            + "Password length must be more than 5 characters.");
        }
        if (personDTO.getPassword() != null) {
            personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        }
        PersonDTO currentDTO = persons.domainToDTO(current.get());
        currentDTO = persons.pathUpdate(currentDTO, personDTO);
        current = Optional.of(persons.dtoToDomain(currentDTO));
        persons.save(current.get());
        return new ResponseEntity<>(
                currentDTO,
                HttpStatus.OK
        );
    }

    /**
     * Обновление Person через DTO
     *
     * @param personDTO PersonDTO
     * @return ResponseEntity
     */
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody PersonDTO personDTO) {
        LOG.info("Update person={}", personDTO);
        if (personDTO.getLogin() == null || personDTO.getPassword() == null) {
            throw new NullPointerException("Invalid login or password");
        }
        if (personDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. "
                            + "Password length must be more than 5 characters.");
        }
        personDTO.setPassword(encoder.encode(personDTO.getPassword()));
        this.persons.save(persons.dtoToDomain(personDTO));
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
