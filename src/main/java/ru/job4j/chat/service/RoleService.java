package ru.job4j.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * RoleService слой бизнес логики модели role.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@Service
public class RoleService implements IService<Role> {
    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class.getSimpleName());
    private final RoleRepository roles;

    public RoleService(RoleRepository roles) {
        this.roles = roles;
    }

    @Override
    public Role save(Role type) {
            return this.roles.save(type);
    }

    @Override
    public Optional<Role> findById(int id) {
        return this.roles.findById(id);
    }

    @Override
    public void delete(Role type) {
        this.roles.delete(type);
    }

    @Override
    public Iterable<Role> findAll() {
        return this.roles.findAll();
    }
}
