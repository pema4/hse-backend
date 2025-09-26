# Семинар 2

Работа с сетью, классы `Socket` и `ServerSocket`.

## Пример 1

Классы [Client](./src/main/java/Client.java) и [Server](./src/main/java/Server.java) -
обмен текстом через сокеты.

## Пример 2

Классы [HeartbeatClient](./src/main/java/HeartbeatClient.java) и [HeartbeatServer](./src/main/java/HeartbeatServer.java) -
реализация упрощённой версии механизма [heartbeat](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_WebSocket_servers#pings_and_pongs_the_heartbeat_of_websockets)
из WebSockets.

## Полезные ссылки/литература

* главы 1-4, 8-9, 12 из Java Network Programming, Fourth Edition by Elliotte Rusty Harold
* современная альтернатива `java.io` - https://square.github.io/okio/
* пример работы с UDP - https://www.baeldung.com/udp-in-java
