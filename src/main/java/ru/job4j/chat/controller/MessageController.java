package ru.job4j.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.MessageDTO;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoomService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 8. Валидация моделей в Spring REST [#504801]
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
    private final RoomService rooms;
    private final PersonService persons;

    public MessageController(RestTemplate rest, MessageService messages,
                             RoomService rooms, PersonService persons) {
        this.rest = rest;
        this.messages = messages;
        this.rooms = rooms;
        this.persons = persons;
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

    /**
     * Создание новой записи через MessageDTO.
     *
     * @param messageDTO MessageDTO
     * @return ResponseEntity.
     */
    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<MessageDTO> create(@Valid @RequestBody MessageDTO messageDTO) {
        LOG.info("Save message={}", messageDTO);
        Person person = persons.findById(messageDTO.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Room room = rooms.findById(messageDTO.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Message message = Message.of(messageDTO.getText(), person, room);
        return new ResponseEntity<>(
                messages.domainToDTO(messages.save(message)),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновление заполненных полей через рефлексию.
     *
     * @param messageDTO DTO модель
     * @return ResponseEntity
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException    exception
     */
    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<MessageDTO> updateMessage(@Valid @RequestBody MessageDTO messageDTO)
            throws InvocationTargetException, IllegalAccessException {
        Message current = messages.findById(messageDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        MessageDTO currentDTO = messages.domainToDTO(current);
        currentDTO = messages.pathUpdate(currentDTO, messageDTO);
        messages.save(messages.dtoToDomain(currentDTO));
        return new ResponseEntity<>(
                currentDTO,
                HttpStatus.OK
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody MessageDTO messageDTO) {
        LOG.info("Update message={}", messageDTO);
        Message message = messages.dtoToDomain(messageDTO);
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
