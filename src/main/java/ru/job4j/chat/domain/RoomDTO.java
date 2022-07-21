package ru.job4j.chat.domain;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 7. DTO [#504800]
 * RoomTDD tdo модели room
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 19.07.2022
 */
public class RoomDTO {
    private Integer id;
    private String name;
    private Integer adminId;

    public static RoomDTO of(Integer id, String name, Integer adminId) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.id = id;
        roomDTO.name = name;
        roomDTO.adminId = adminId;
        return roomDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
