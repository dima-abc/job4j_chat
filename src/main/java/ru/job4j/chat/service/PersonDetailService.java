package ru.job4j.chat.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoleRepository;

import java.util.Collections;
import java.util.List;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * PersonDetailService этот сервис будет загружать
 * в SecurityHolder детали авторизованного пользователя.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 16.07.2022
 */
@Service
public class PersonDetailService implements UserDetailsService {
    private final PersonRepository persons;

    public PersonDetailService(PersonRepository persons) {
        this.persons = persons;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = this.persons.findPersonByLogin(username);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.getLogin(), person.getPassword(), Collections.emptyList());
    }
}
