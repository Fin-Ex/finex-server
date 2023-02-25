# FinEx Server

Abstract MMO server written in Java.

Our short MVP:
1. Implement a base set of MMO servers: auth-server, world-server, ai-server
2. Provides a high available and ready to high load: scaling server applications 
3. Easy-to-write gameplay logic: all gameplay logic didn't interact directly with multithreading, atomic states and other complex entities.
4. Works on classical ticks, automatic paralleling not crossed jobs in one tick.

There is a third generation of FinEx MMO servers.
 - FinEx Server (third generation)
 - KT Server (second generation, abandoned)
 - WGP Server (first generation)

## Requirements
 - Java 17+
 - PostgreSQL 12+
 - CPU with [AVX2](https://en.wikipedia.org/wiki/Advanced_Vector_Extensions#CPUs_with_AVX2) support

## Links
 - [Trello](https://trello.com/b/MCEIOoTQ/finex-server)

## Project Modules
### Core
Project core.
Provide:
 - DI/IoC
 - Math (BVH, shapes, vectors, planes, quaternions, specific math operations)
 - Configuration ([HOCON](https://github.com/lightbend/config))
 - Entity component system
 - Native client network
 - Persistence (JPA, repositories, [database migrations](https://github.com/Fin-Ex/finex-evolution))
 - Pools
 - Logging

And many other features.

### Relay server
Optional relay server.
Point to enter into cluster for game clients. Process native client protocol, translating it into Data Transfer Object and publish it to server bus. 

Also, can be using as standalone application to connect game clients with point-to-point model.

### Auth server
Authorization server.

### World server
World server.

### Testing
JUnit extension library. 
Provides docker containers with Redis & PostgreSQL and allow starting server.

### Netty-Network
Small network facade between [Netty](https://github.com/netty/netty) and core. 
Provide interfaces and upper service to control Netty.

## Build
To build project your must enable preview features for maven:
```
--enable-preview --add-modules jdk.incubator.foreign --add-modules jdk.incubator.vector
```
 - Vector module using to math vectors (128/256 bit registers).
 - Foreign module using to work with native libraries (physic engines)