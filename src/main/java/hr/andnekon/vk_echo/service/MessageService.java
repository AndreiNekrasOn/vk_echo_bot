package hr.andnekon.vk_echo.service;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import hr.andnekon.vk_echo.dto.EventDto;


/**
 * Сервис отправки сообщений
 * Обращается к Vk API
 */
@Service
public class MessageService {

    // Ключ для доступа к API
    @Value("${appAccessKey}")
    private String accessKey;

    // Версия API
    @Value("${appApiVersion}")
    private String apiVersion;

    // URL для обращения к методу API отправки сообщений
    private static final String API_URL = "https://api.vk.com/method/messages.send";

    // Рест-клиент для обращения к API
    @Autowired
    private final RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Отправляет обертку эхо-сообщения в формате x-www-form-urlencoded
     * @param event - событие от Callback-api
     */
    public void reply(EventDto event) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + accessKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("v", apiVersion);
        body.add("random_id", event.getObject().getMessage().getId());
        body.add("peer_id", event.getObject().getMessage().getFromId());
        body.add("message", MessageFormat.format("Вы сказали: {0}", event.getObject().getMessage().getText()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
        logger.info(response.getBody());
    }
}

