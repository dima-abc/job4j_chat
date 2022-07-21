package ru.job4j.chat.domain;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 7. DTO [#504800]
 * PersonDTO TDO модели Person.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 19.07.2022
 */
public class PersonDTO {
    private Integer id;
    private String login;
    private String password;
    private Integer roleId;

    public static PersonDTO of(Integer id, String login, String password, Integer roleId) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.id = id;
        personDTO.login = login;
        personDTO.password = password;
        personDTO.roleId = roleId;
        return personDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
