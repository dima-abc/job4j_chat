package ru.job4j.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 8. Валидация моделей в Spring REST [#504801]
 * Person пользователь чата.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 12.07.2022
 */
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Login must be not empty")
    private String login;
    @Column(nullable = false)
    @NotBlank(message = "Password must be not empty")
    @Size(min = 6, message = "Invalid password. Password length must be more than 5 characters.")
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @NotNull(message = "Role must be not null")
    private Role role;

    public static Person of(String login, String password, Role role) {
        Person person = new Person();
        person.login = login;
        person.password = password;
        person.role = role;
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{id=" + id + ", login='" + login + '\''
                + ", password='" + password + '\''
                + ", role=" + role + '}';
    }
}
