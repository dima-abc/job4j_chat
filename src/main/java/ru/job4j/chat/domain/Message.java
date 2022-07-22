package ru.job4j.chat.domain;

import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * Message модель данных сообщения в чате.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 12.07.2022
 */
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @Column(name = "message_text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @NotNull(message = "Person must be not null")
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @NotNull(message = "Room must be not null")
    private Room room;

    public static Message of(String text, Person person, Room room) {
        Message message = new Message();
        message.text = text;
        message.person = person;
        message.room = room;
        return message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{id=" + id + ", text='" + text + '\''
                + ", person=" + person + ", room=" + room + '}';
    }
}
