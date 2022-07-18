package ru.job4j.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.service.MessageService;

import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * MessageController rest api модели message.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 13.07.2022
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class.getSimpleName());
    private final RestTemplate rest;
    private final MessageService messages;
    private static final String API_PERSON_ID = "http://localhost:8080/person/{id}";
    private static final String API_ROOM_ID = "http://localhost:8080/room/{id}";

    public MessageController(RestTemplate rest, MessageService messages) {
        this.rest = rest;
        this.messages = messages;
    }

    @GetMapping("/")
    public Iterable<Message> findAll() {
        LOG.info("Find all message");
        Iterable<Message> result = this.messages.findAll();
        for (Message message : result) {
            Person person = this.rest.getForObject(API_PERSON_ID, Person.class, message.getPerson().getId());
            Room room = this.rest.getForObject(API_ROOM_ID, Room.class, message.getRoom().getId());
            message.setPerson(person);
            message.setRoom(room);
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        LOG.info("Message find by id={}", id);
        Optional<Message> message = this.messages.findById(id);
        message.ifPresent(m -> {
            Person person = this.rest.getForObject(
                    API_PERSON_ID, Person.class,
                    m.getPerson().getId());
            Room room = this.rest.getForObject(
                    API_ROOM_ID, Room.class,
                    m.getRoom().getId());
            m.setPerson(person);
            m.setRoom(room);
        });
        return new ResponseEntity<>(
                message.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message is not found. Please, check requisites."
                )),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        LOG.info("Save message={}", message);
        return new ResponseEntity<>(
                this.messages.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        LOG.info("Update message={}", message);
        this.messages.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        LOG.info("Delete message by id={}", id);
        Message message = new Message();
        message.setId(id);
        this.messages.delete(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{id}")
    public List<Message> findAllByRoom(@PathVariable int id) {
        LOG.info("Find all message by room_id={}", id);
        Room room = this.rest.getForObject(API_ROOM_ID, Room.class, id);
        List<Message> result = this.messages.fidAllByRoom(room);
        for (Message message : result) {
            message.setRoom(room);
            Person person = this.rest.getForObject(API_PERSON_ID, Person.class, message.getPerson().getId());
            message.setPerson(person);
        }
        return result;
    }

    @GetMapping("/person/{id}")
    public List<Message> findAllByPerson(@PathVariable int id) {
        LOG.info("Find all message by person_id={}", id);
        Person person = this.rest.getForObject(API_PERSON_ID, Person.class, id);
        List<Message> result = this.messages.findAllByPerson(person);
        for (Message message : result) {
            message.setPerson(person);
            Room room = this.rest.getForObject(API_ROOM_ID, Room.class, message.getRoom().getId());
            message.setRoom(room);
        }
        return result;
    }
}
