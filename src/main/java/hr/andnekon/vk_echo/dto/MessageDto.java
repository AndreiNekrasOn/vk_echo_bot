package hr.andnekon.vk_echo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для тела события обработки сообщений
 */
public class MessageDto {
    // Идентификатор отправителя
    @JsonProperty("from_id")
    private int fromId;

    // Текст сообщения
    private String text;

    // Идентификатор сообщения
    private int id;

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

