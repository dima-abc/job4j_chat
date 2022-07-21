package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;
import ru.job4j.chat.domain.RoomDTO;
import ru.job4j.chat.repository.RoomRepository;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * RoomService слой бизнес логики модели room
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@Service
public class RoomService implements IService<Room, RoomDTO> {
    private final RoomRepository rooms;

    public RoomService(RoomRepository rooms) {
        this.rooms = rooms;
    }

    @Override
    public Room save(Room type) {
        return this.rooms.save(type);
    }

    @Override
    public Optional<Room> findById(int id) {
        return this.rooms.findById(id);
    }

    @Override
    public void delete(Room type) {
        this.rooms.delete(type);
    }

    @Override
    public Iterable<Room> findAll() {
        return this.rooms.findAll();
    }

    @Override
    public Room dtoToDomain(RoomDTO dto) {
        Person admin = new Person();
        admin.setId(dto.getAdminId());
        Room room = Room.of(dto.getName(), admin);
        room.setId(dto.getId());
        return room;
    }

    @Override
    public RoomDTO domainToDTO(Room domain) {
        return RoomDTO.of(domain.getId(), domain.getName(), domain.getAdmin().getId());
    }
}
