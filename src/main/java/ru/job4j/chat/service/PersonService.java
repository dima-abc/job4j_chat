package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * PersonService слой бизнес логики модели person.
 *
 * @author Dmitry Stepanov, user Dmitry
 * since 13.07.2022
 */
@Service
public class PersonService implements IService<Person> {
    private final PersonRepository persons;

    public PersonService(PersonRepository persons) {
        this.persons = persons;
    }

    @Override
    public Person save(Person type) {
        return this.persons.save(type);
    }

    @Override
    public Optional<Person> findById(int id) {
        return this.persons.findById(id);
    }

    @Override
    public void delete(Person type) {
        this.persons.delete(type);
    }

    @Override
    public Iterable<Person> findAll() {
        return this.persons.findAll();
    }
}
