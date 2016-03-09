# stateless-rest-jwtcookie-demo
Simple Spring Boot REST Api example, with Swagger UI enabled that exposes Spring Security login/logout endpoints, and sample endpoints for public, authenticated and ADMIN restriction based acces.

It does not use JSESSIONID, but a cookie that stores a JWT that is used to restore user credentials.
- dev.axt.demo.security.SecurityConfig contains the relevant code to the stateless jwt cookie based authentication with spring security.

It's a ready to run, so:

´´´

mvn spring-boot:run

´´´

After it's loaded, http://localhost:18080/swagger-ui.html

Available users are:
- admin/test
- user/test

Configuration (config/application-default.properties) activates the load of necessary schema in a in-memory h2 database (src/main/resources/schema.sql)




 


