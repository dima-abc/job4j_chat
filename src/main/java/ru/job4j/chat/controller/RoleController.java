package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * RoleController rest api модели role.
 * 5. Обработка исключений и Spring REST [#504797]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class.getSimpleName());
    private final RoleService roles;
    private final ObjectMapper objectMapper;

    public RoleController(RoleService roles, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        LOG.info("Create role={}", role);
        if (role.getName() == null) {
            throw new NullPointerException("Invalid role name");
        }
        if (!role.getName().startsWith("ROLE_")) {
            throw new IllegalArgumentException("Invalid Role name. Role name start with 'ROLE_'");
        }
        return new ResponseEntity<>(
                this.roles.save(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        LOG.info("Update role={}", role);
        if (role.getName() == null) {
            throw new NullPointerException("Invalid role name");
        }
        if (!role.getName().startsWith("ROLE_")) {
            throw new IllegalArgumentException("Invalid Role name. Role name start with 'ROLE_'");
        }
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

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOG.error(e.getLocalizedMessage());
    }
}
