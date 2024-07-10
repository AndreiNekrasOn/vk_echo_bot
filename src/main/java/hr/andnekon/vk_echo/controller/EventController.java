package hr.andnekon.vk_echo.controller;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hr.andnekon.vk_echo.dto.EventDto;
import hr.andnekon.vk_echo.service.MessageService;

/**
 * Контроллер обработки событий
 */
@RestController
public class EventController {
    // TODO: get appAuthToken from the groups.getCallbackConfirmationCode

    // Ключ доступа к API
    @Value("${appAuthToken}")
    private String authToken;

    // Идентификатор сообщества
    @Value("${appGroupId}")
    private Integer groupId;

    // Сервис отправки сообщений
    private final MessageService messageService;

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    public EventController(MessageService messageService) {
        this.messageService = messageService;
    }


    /**
     * Обработка событий Callback-api
     * @param event - событие
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<String> process(@RequestBody EventDto event) {
        switch (event.getType()) {
            case "confirmation":
                if (groupId.intValue() != event.getGroupId().intValue()) {
                    logger.info("Confirmation with incorrect group");
                    return ResponseEntity.badRequest().build();
                }
                logger.info("Confirmation success");
                return ResponseEntity.ok(authToken);
            case "message_new":
                logger.info(MessageFormat.format("Recieved message: {0}",
                        event.getObject().getMessage().getText()));
                messageService.reply(event);
                return ResponseEntity.ok("ok");
            default:
                logger.info("Bad request");
                return ResponseEntity.badRequest().build();
        }
    }
}
