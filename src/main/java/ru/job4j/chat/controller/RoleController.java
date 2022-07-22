package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 5. Обработка исключений и Spring REST [#504797]
 * 8. Валидация моделей в Spring REST [#504801]
 * RoleController rest api модели role.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class.getSimpleName());
    private final RoleService roles;

    public RoleController(RoleService roles) {
        this.roles = roles;
    }

    @GetMapping("/")
    public Iterable<Role> findAll() {
        LOG.info("Find all Role");
        return this.roles.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        LOG.info("Find role by id={}", id);
        Optional<Role> role = this.roles.findById(id);
        return new ResponseEntity<>(
                role.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role is not found. Please, check requisites."
                )),
                HttpStatus.OK
        );
    }

    /**
     * Создание новой записи через Map и DTO
     *
     * @param role Role
     * @return ResponseEntity
     */
    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        LOG.info("Create role={}", role);
        return new ResponseEntity<>(
                this.roles.save(role),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновление заполненных полей через рефлексию
     *
     * @param role Role
     * @return ResponseEntity
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException    exception
     */
    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Role> updatePatch(@Valid @RequestBody Role role) throws InvocationTargetException, IllegalAccessException {
        Role current = roles.findById(role.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        current = roles.pathUpdate(current, role);
        return new ResponseEntity<>(
                roles.save(current),
                HttpStatus.OK
        );
    }

    /**
     * Обновление модели через Map DTO.
     *
     * @param role Role
     * @return ResponseEntity
     */
    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Role role) {
        LOG.info("Update role={}", role);
        this.roles.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        LOG.info("Delete role by id={}", id);
        Role role = new Role();
        role.setId(id);
        this.roles.delete(role);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
