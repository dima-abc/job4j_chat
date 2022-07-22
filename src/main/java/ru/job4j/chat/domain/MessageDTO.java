package ru.job4j.chat.domain;

import ru.job4j.chat.handlers.Operation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * 7. DTO [#504800]
 * 8. Валидация моделей в Spring REST [#504801]
 * MessageDTO TDO модели Message.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 19.07.2022
 */
public class MessageDTO {
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private Integer id;
    private String text;
    @NotNull(message = "Person ID must be not null")
    private Integer personId;
    @NotNull(message = "Room ID must be non null")
    private Integer roomId;

    public static MessageDTO of(Integer id, String text, Integer personId, Integer roomId) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.id = id;
        messageDTO.text = text;
        messageDTO.personId = personId;
        messageDTO.roomId = roomId;
        return messageDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
