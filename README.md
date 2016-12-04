# sample-todo
Sample TODO single page app using Spring Boot, Spring Data Rest, React, ES6, ...

## Setup
To build the project be sure to have `maven`, `npm` and `grunt` installed and in your `PATH`

## Dev build
Run `mvn package` in the project root dir. Contains source maps and developer React code.

## Prod build
Run with `prod` profile: `mvn package -P prod`.

## Run
Find built jar in `target` dir and run `java -jar sample-todo-app-<version>.jar`. Be aware that it will create log files/dirs in the pwd.

####How to change the server port 
Add `--server.port=<your-port>` to the command args.

####Where is the database ?
App uses the in memory db `h2`. Access the web console on `http://localhost:8080/h2`. Be sure to set `jdbc:h2:mem:testdb` as `JDBC URL`.


