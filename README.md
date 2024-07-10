# VKEchoBot
Бот для отправки эхо сообщений с использованием vk-api
...без использования готовых библиотек для реализации VkApi

## Запуск приложения

Необходимо создать файл .env:

```
export appAccessKey=YOUR_ACCESS_KEY
export appAuthToken=YOUR_TOKEN
export appGroupId=YOUR_GROUP_ID
export apiVersion=5.199
```

- YOUR_ACCESS_KEY - Управление > Настройки > Работа с API > Ключ доступа
- YOUR_TOKEN - Управление > Настройки > Работа с API > Callback API > "Строка, которую должен вернуть сервер"
- YOUR_GROUP_ID - Управление > Настройки > Работа с API > Callback API > Взять значение из group_id

Для запуска приложения нужно инициализировать переменные окружения и выполнить команду:

```
source .env
./mvnw spring-boot:run
```

Если вы используете Windows — вместо `export` должен быть `set`, вместо `source .env` — `PATH > .env`. Для Windows работа приложения не проверялась.

Для запуска тестов:

```
./mvnw test
```
