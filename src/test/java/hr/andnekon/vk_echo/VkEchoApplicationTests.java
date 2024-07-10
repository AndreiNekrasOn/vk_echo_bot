package hr.andnekon.vk_echo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import hr.andnekon.vk_echo.dto.EventDto;
import hr.andnekon.vk_echo.dto.MessageDto;
import hr.andnekon.vk_echo.dto.ObjectDto;
import hr.andnekon.vk_echo.service.MessageService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class VkEchoApplicationTests {

    // Токен аутентификации
    @Value("${appAuthToken}")
    private String authToken;

    // Ключ доступа к API
    @Value("${appAccessKey}")
    private String accessKey;

    // Версия API
    @Value("${appApiVersion}")
    private String apiVersion;

    // Идентификатор сообщества
    @Value("${appGroupId}")
    private Integer groupId;

    // Контекст приложения
    @Autowired
    public WebApplicationContext context;

    // Мок контроллера
    private MockMvc mockMvc;

    // Мок рест-клиента для запросов к API
    private MockRestServiceServer mockServer;


    // Мок рест-клиента для обращения к API
    @Mock
    @Autowired
    private RestTemplate restTemplate;


    // Сервис отправки сообщений
    @InjectMocks
    @Spy
    @Autowired
    private MessageService messageService;

    // URL для обращения к методу API отправки сообщений
    private static final String API_URL = "https://api.vk.com/method/messages.send";

    @BeforeEach
    public void init() {
        this.mockServer = MockRestServiceServer.bindTo(this.restTemplate).build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * Тест аутентификации приложения
     * @throws Exception
     */
    @Test
    void testAppAuthentication() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("type", "confirmation");
        payload.put("group_id", groupId);
        ResultActions result = mockMvc.perform(post("/")
            .contentType("application/json")
            .content(payload.toString()))
        .andExpect(status().isOk());
        String response = result.andReturn().getResponse().getContentAsString();
        assertEquals(authToken, response);
    }

    /**
     * Тест отправки эхо-сообщения
     * @throws Exception
     */
    @Test
    void testMessageEcho() throws Exception {
        MessageDto message = new MessageDto();
        message.setText("Hello, world");
        message.setId(5);
        message.setFromId(1);

        ObjectDto object = new ObjectDto();
        object.setMessage(message);

        EventDto payload = new EventDto();
        payload.setType("message_new");
        payload.setObject(object);

        StringBuilder expected = new StringBuilder();
        expected.append("v=").append(apiVersion).append("&");
        expected.append("random_id=").append(payload.getObject().getMessage().getId()).append("&");
        expected.append("peer_id=").append(payload.getObject().getMessage().getFromId()).append("&");
        expected.append("message=").append(URLEncoder.encode(
            MessageFormat.format("Вы сказали: {0}", payload.getObject().getMessage().getText()),
            "UTF-8"));

        mockServer.expect(requestTo(API_URL))
            .andExpect(header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"))
            .andExpect(header("Authorization", "Bearer " + accessKey))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(expected.toString()))
            .andRespond(withSuccess());
        messageService.reply(payload);
        mockServer.verify();
    }

}
