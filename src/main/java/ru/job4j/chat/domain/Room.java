package ru.job4j.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 8. Валидация моделей в Spring REST [#504801]
 * Room модель данных комнаты чата (один из множества чатов)
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 12.07.2022
 */
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @Column(name = "room_name", nullable = false)
    @NotBlank(message = "Name must be non empty")
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @NotNull(message = "Admin  must be non null")
    private Person admin;

    public static Room of(String name, Person admin) {
        Room room = new Room();
        room.name = name;
        room.admin = admin;
        return room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getAdmin() {
        return admin;
    }

    public void setAdmin(Person admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Room{id=" + id + ", name='" + name + '\''
                + ", admin=" + admin + '}';
    }
}
