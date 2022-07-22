package ru.job4j.chat.domain;

import ru.job4j.chat.handlers.Operation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 7. DTO [#504800]
 * 8. Валидация моделей в Spring REST [#504801]
 * RoomTDD tdo модели room
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 19.07.2022
 */
public class RoomDTO {
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private Integer id;
    @NotBlank(message = "Name must be not empty")
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
