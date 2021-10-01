# Configuration
## Base configuration file
Create a file `application.conf` with content:
```hocon
include "application-world.conf"

# your parameters...
```

Imported resource (`application-world.conf`) contains default world-server parameters with environment support.

Server supports a few configuration methods (with priority to load):
1. `resources/application.conf` from file system
2. `application.conf` from jar 

### Environment parameters
- `THREAD_GAME_MIN` - minimal threads of game-logic executor (`4`)
- `THREAD_GAME_MAX` - maximal threads of game-logic executor (`8`)
- `THREAD_SERVICE_MIN` - minimal threads of service executor (`2`)
- `THREAD_SERVICE_MAX` - maximal threads of service executor (`4`)
- `DB_URL` - jdbc url (postgresql)
- `DB_SCHEMA` - database schema (`world`)
- `DB_USER` - database username
- `DB_PASSWORD` - database password
- `REDIS_ADDRESS` - redis address (`redis://localhost:6379`)
- `REDIS_USER` - redis username
- `REDIS_PASSWORD` - redis password
- `CLUSTER_RETRY_COUNT` - count of retry connection if redis unavailable (`3`)
- `CLUSTER_RETRY_INTERVAL` - retry interval in millis (`600`)
- `CLUSTER_DNS_REFRESH_INTERVAL` - local cache DNS refresh time (`5000`)
- `CLUSTER_THREADS` - count of operative threads (`4`)
- `CLUSTER_SUBSCRIPTIONS` - redis subscriptions per connection (`5`)
- `CLUSTER_MIN_SIZE` - minimal count of subscription connections (`1`)
- `CLUSTER_MAX_SIZE` - maximal count of subscription connections (`20`)
- `CLUSTER_IDLE_CONNECTION_TIMEOUT` - time to remove connection if it did not use, in millis (`10000`)
- `CLUSTER_CONNECTION_TIMEOUT` - connection timeout in millis (`5000`)
- `CLUSTER_TIMEOUT` - time to wait response from redis in millis (`1000`)
- `CLUSTER_PING_INTERVAL` - ping-pong interval in millis (`30000`)
- `CLUSTER_KEEP_ALIVE` - use TCP keep-alive for redis connection (`true`)
- `CLUSTER_NO_DELAY` - use TCP no-delay for redis connection (`false`)
- `CLUSTER_CONNECTION_MIN_SIZE` - minimal connections to redis (`4`)
- `CLUSTER_CONNECTION_MAX_SIZE` - maximal connections to redis (`64`)
- `CLUSTER_NETWORK_THREADS` - network threads to redis interaction (`4`)
- `CLUSTER_TRANSPORT` - netty transport type (`NIO`), values: `NIO`, `EPOLL`, `KQUEUE`

## Logging
FinEx server is supports custom logback configuration. 
Create a file `logback.xml` with content:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <include resource="finex-logback-default.xml"/>

    <root level="info">
        <appender-ref ref="CONSOLE"/>
    </root>
	
</configuration>
```

Appender `CONSOLE` defined in imported resource `finex-logback-default.xml`, its default console appender.