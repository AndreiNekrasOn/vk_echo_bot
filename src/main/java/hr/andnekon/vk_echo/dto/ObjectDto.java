package hr.andnekon.vk_echo.dto;

/**
 * DTO промежуточного слоя события обработки сообщений
 */
public class ObjectDto {
    // Сообщение
    private MessageDto message;

    public MessageDto getMessage() {
        return message;
    }

    public void setMessage(MessageDto message) {
        this.message = message;
    }
}

