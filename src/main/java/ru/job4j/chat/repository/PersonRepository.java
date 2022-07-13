package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domain.Person;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * PersonRepository CRUD модели данных person.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 13.07.2022
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
}
