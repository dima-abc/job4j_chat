package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domain.Message;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Room;

import java.util.List;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * MessageRepository CRUD модели данных message.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 13.07.2022
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findAllByRoom(Room room);

    List<Message> findAllByPerson(Person person);
}
