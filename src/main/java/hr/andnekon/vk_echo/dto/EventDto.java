package hr.andnekon.vk_echo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO события Callback-api
 */
public class EventDto {

    // Тип события
    private String type;

    // Идентификатор сообщества
    @JsonProperty("group_id")
    private Integer groupId;

    // Промежуточный слой для события сообщения
    private ObjectDto object;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public ObjectDto getObject() {
        return object;
    }

    public void setObject(ObjectDto object) {
        this.object = object;
    }
}

