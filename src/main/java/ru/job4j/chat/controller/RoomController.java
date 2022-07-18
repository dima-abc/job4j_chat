package ru.job4j.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.service.RoomService;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * RoomController rest api модели role.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class.getSimpleName());
    private static final String API_PERSON_ID = "http://localhost:8080/person/{id}";
    private final RestTemplate rest;
    private final RoomService rooms;

    public RoomController(RestTemplate rest, RoomService rooms) {
        this.rest = rest;
        this.rooms = rooms;
    }

    @GetMapping("/")
    public Iterable<Room> findAll() {
        LOG.info("Find all Room");
        Iterable<Room> result = this.rooms.findAll();
        for (Room room : result) {
            room.setAdmin(
                    this.rest.getForObject(
                            API_PERSON_ID,
                            Person.class,
                            room.getAdmin().getId()));
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        LOG.info("Room find by id={}", id);
        Optional<Room> room = this.rooms.findById(id);
        room.ifPresent(r -> r.setAdmin(
                this.rest.getForObject(
                        API_PERSON_ID,
                        Person.class,
                        room.get().getAdmin().getId())));
        return new ResponseEntity<>(
                room.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room is not found. Please, check requisites."
                )),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        LOG.info("Create room={}", room);
        if (room.getName() == null) {
            throw new NullPointerException("Invalid room name");
        }
        return new ResponseEntity<>(
                this.rooms.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        LOG.info("Update room={}", room);
        if (room.getName() == null) {
            throw new NullPointerException("Invalid room name");
        }
        this.rooms.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        LOG.info("Delete room by id={}", id);
        Room room = new Room();
        room.setId(id);
        this.rooms.delete(room);
        return ResponseEntity.ok().build();
    }
}
