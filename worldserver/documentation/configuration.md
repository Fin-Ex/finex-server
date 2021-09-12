# Configuration
## Base configuration file
Create a file `application.conf` with content:
```hocon
include "application-world.conf"

some.parameter: "value"
```

Imported resource (`application-world.conf`) contains default world-server parameters with environment support.

Server supports a few configuration methods (with priority to load):
1. `resources/application.conf` in file system
2. `application.conf` in jar 

### Environment parameters
- `THREAD_GAME_MIN` - minimal threads of game-logic executor
- `THREAD_GAME_MAX` - maximal threads of game-logic executor
- `THREAD_SERVICE_MIN` - minimal threads of service executor
- `THREAD_SERVICE_MAX` - maximal threads of service executor
- `CLUSTER_ADDRESS` - IP range of all cluster nodes
- `CLUSTER_INTERFACE` - network interface to bind
- `DB_URL` - jdbc url (postgresql)
- `DB_SCHEMA` - database schema
- `DB_USER` - database username
- `DB_PASSWORD` - database password

## Logging
FinEx server is supports custom logback configuration. 
Create a file `logback.xml` with content:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <include resource="colored-console.xml"/>

    <root level="info">
        <appender-ref ref="CONSOLE"/>
    </root>
	
</configuration>
```

Appender `CONSOLE` defined in imported resource `colored-console.xml`, its default console appender.