package ru.job4j.chat.service;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * IService интерфейс бизнес логики
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
public interface IService<T> {
    T save(T type);

    Optional<T> findById(int id);

    void delete(T type);

    Iterable<T> findAll();
}
