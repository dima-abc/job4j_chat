package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domain.Role;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * RoleRepository CRUD модели данных role.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 13.07.2022
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
