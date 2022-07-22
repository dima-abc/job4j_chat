package ru.job4j.chat.domain;

import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 8. Валидация моделей в Spring REST [#504801]
 * Role модель данный роли пользователя (ROLE_ADMIN, ROLE_USER)
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 12.07.2022
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @Column(name = "role_name", nullable = false, unique = true)
    @Pattern(regexp = "^ROLE_.*$", message = "Role name must start with ROLE_")
    private String name;

    public static Role of(String name) {
        Role role = new Role();
        role.name = name;
        return role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + name + '\'' + '}';
    }
}
