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

# Requirements
 - Java 17+
 - PostgreSQL