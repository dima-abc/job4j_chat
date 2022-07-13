package ru.job4j.chat.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
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
    private int id;
    @Column(name = "room_name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
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
