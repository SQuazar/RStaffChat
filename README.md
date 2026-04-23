# RStaffChat - Распределённый стафф-чат!

RStaffChat реализует распределённую систему на основе Redis Pub/Sub,
что делает его максимально гибким к любой интеграции (Discord, Telegram, etc..) \
Для работы плагина необходимо поднять свой Redis сервер. В качестве примера привету `docker-compose.yml`.
Вы можете поднять свой Redis сервер и другими способами, если таковые будут более удобны для вас.

```yaml
services:
  redis:
    image: redis:7.2
    container_name: redis
    ports:
      - "6379:6379"
    restart: unless-stopped
    command: [ "redis-server", "--requirepass", "password" ]
```

> [!CAUTION]
> Позаботьтесь о безопасности своего Redis сервера!

## Конфигурация

Плагин предоставляет стандартную конфигурацию при первом запуске

```yaml
server-name: MC-1
redis-uri: redis://localhost:6379

# Только MiniMessage Format
format: "<red>[<source>] [STAFF] <gray><sender> -> <yellow><message>"
```

> [!TIP]
> Для установки ссылки на защищенный Redis можете воспользоваться следующим шаблоном
> `redis://:YOUR_PASSWORD@localhost:6379`

> [!NOTE]
> Для идентифицирования вашего сервера, воспользуйтесь параметром `server-name`

> [!WARNING]
> Параметр `format` поддерживает только [MiniMessage Format](https://docs.papermc.io/adventure/minimessage/). Убедитесь,
> что используете только его для форматирования сообщений

## Возможности плагина

* Нет зависимости от прокси сервера, может работать на нескольких независимых серверах
* Полностью асинхронная работа с Redis
* Открытое API для расширения функционала сторонними разработчиками
* Поддержка PlaceholderAPI
* Полная поддержка MiniMessage
* Легок для расширения независимо от платформы, можно подключить своё ПО, которое будет работать с событиями плагина

## Команды и права

| Команда            | Право       | Описание                                                                   |
|--------------------|-------------|----------------------------------------------------------------------------|
| `/staff <message>` | rstaff.chat | Отправить сообщение в стафф-чат                                            |
| `/rstaff`          | rstaff.amin | Перезагрузить конфигурацию плагина (не используется для смены `redis-uri`) |


## Использование API

Для начала использования API системы в контексте плагина, вам необходимо получить экземпляр класса `RStaffChatAPI`
```java
    private RStaffChatAPI api;
    
    public boolean setupApi() {
        RegisteredServiceProvider<RStaffChatAPI> provider = Bukkit.getServicesManager().getRegistration(RStaffChatAPI.class);
        if (provider == null) return false;
        this.api = provider.getProvider();
        return true;
    }
```

Аналогично и для `StaffChatService`.

## Сборка плагина

Для сборки плагина вы можете воспользоваться командой

```shell
./gradlew clean build
```

Конечный `jar` файл плагина будет лежать в `plugin/build/libs/`

## Баги и обратная связь

Для информировании об ошибках или недочетах,
используйте [GitHub Issues](https://github.com/SQuazar/RStaffChat/issues)

## Контакты

* [Telegram](https://t.me/squazar)
* [SpigotRU](https://spigotmc.ru/members/nullpointer.5912/)