# FinEx Server World
## Environment parameters
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

## Configuration file
All configuration of world server contains into `application-world.conf`. Be free to include it to `application.conf`.