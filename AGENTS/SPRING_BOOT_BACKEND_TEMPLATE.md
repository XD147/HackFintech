## Spring Boot Backend Generation Template

Este documento define **cómo un agente de IA debe generar un backend completo en Spring Boot** siguiendo una arquitectura profesional.

Debe usarse junto con `AGENTS.md`.

El objetivo es permitir que un agente genere **un backend completo y consistente**.

---

# 1. Estructura final del proyecto

El agente debe generar la siguiente estructura:

```
src/main/java/com/project

config
controller
service
repository
entity
dto
security
exception

ProjectApplication.java
```

Estructura completa:

```
src
 └─ main
     └─ java
         └─ com.project
             ├─ config
             ├─ controller
             ├─ service
             ├─ repository
             ├─ entity
             ├─ dto
             ├─ security
             ├─ exception
             └─ ProjectApplication.java

resources
 ├─ application.yml
 └─ data.sql
```

---

# 2. Dependencias Maven

El agente debe generar `pom.xml` con las dependencias necesarias.

---

# 3. Entidades JPA

Reglas:

* usar `@Entity`
* usar `@Table`
* usar `@Id`
* usar `@GeneratedValue`

Ejemplo:

```
@Entity
@Table(name = "users")
@Data
public class User {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;

 private String email;

 private String password;
}
```

---

# 4. Repositories

Los repositories deben extender:

```
JpaRepository<Entity, ID>
```

Ejemplo:

```
@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

 Optional<User> findByEmail(String email);

}
```

---

# 5. DTOs

Los DTOs deben separarse en:

### Requests

```
CreateUserRequest
LoginRequest
RegisterRequest
```

### Responses

```
UserResponse
AuthResponse
```

Ejemplo:

```
@Data
public class LoginRequest {

 private String email;
 private String password;

}
```

---

# 6. Services

Los services deben contener **lógica de negocio**.

Reglas:

* usar `@Service`
* usar `@RequiredArgsConstructor`
* nunca acceder directamente desde controller a repository

Ejemplo:

```
@Service
@RequiredArgsConstructor
public class UserService {

 private final UserRepository repository;

 public List<User> findAll() {
  return repository.findAll();
 }

}
```

---

# 7. Controllers

Los controllers deben:

* usar `@RestController`
* usar `@RequestMapping`
* devolver `ResponseEntity`
* Usar @ResponseStatus 
* Usar @PreAuthorize 
* Usar @Operation  

Ejemplo:

```
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

 private final UserService service;

 @GetMapping
 public ResponseEntity<List<User>> findAll() {
  return ResponseEntity.ok(service.findAll());
 }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear", operationId = "crear")
    public ResponseEntity crear(@Valid @RequestBody EntityRequest datos) {
        return ResponseEntity.ok(service.crear(datos).get());
    }

}
```

---

# 8. Seguridad JWT

El sistema debe generar:

```
JwtService
JwtAuthenticationFilter
SecurityConfig
CustomUserDetailsService
```

---

# 9. JwtService

Debe:

* generar tokens
* validar tokens
* extraer username

Funciones mínimas:

```
generateToken()
extractUsername()
isTokenValid()
```

---

# 10. JwtAuthenticationFilter

Debe:

* interceptar requests
* leer header Authorization
* validar token
* setear Authentication en SecurityContext

Debe extender:

```
OncePerRequestFilter
```

---

# 11. SecurityConfig

Debe configurar:

```
SecurityFilterChain
PasswordEncoder
AuthenticationManager
```

Ejemplo de reglas:

```
/auth/** → público
/swagger-ui/** → público
/users/** → autenticado
/admin/** → ADMIN
```

---

# 12. AuthController

Debe exponer endpoints:

```
POST /auth/login
POST /auth/register
```

Respuesta:

```
AuthResponse
```

---

# 13. Manejo global de errores

Debe existir:

```
GlobalExceptionHandler
```

con:

```
@RestControllerAdvice
```

Errores manejados:

```
ValidationException
EntityNotFoundException
UnauthorizedException
```

---

# 14. Swagger

Debe habilitar:

```
springdoc-openapi
```

Acceso esperado:

```
/swagger-ui/index.html
```

---

# 15. application.yml

Debe incluir:

```
spring:
 datasource:
  url: jdbc:mysql://localhost:3306/app
  username: root
  password: root

 jpa:
  hibernate:
   ddl-auto: update
  show-sql: true

jwt:
 secret: secretkey
 expiration: 86400000
```

---

# 16. Testing

El agente debe generar tests básicos.

### Service tests

```
UserServiceTest
```

### Controller tests

```
UserControllerTest
```

### Repository tests

```
UserRepositoryTest
```

Usar:

```
@SpringBootTest
@DataJpaTest
@AutoConfigureMockMvc
```

---

# 17. Convenciones de nombres

Entidades:

```
User
Product
Order
```

DTOs:

```
CreateUserRequest
UserResponse
LoginRequest
AuthResponse
```

Services:

```
UserService
AuthService
```

Controllers:

```
UserController
AuthController
```

---

# 18. Regla final

Antes de generar el backend, el agente debe confirmar:

* entidades
* endpoints
* roles
* autenticación
* base de datos

Si algo falta, debe **seguir preguntando**.

Solo cuando todo esté definido debe **generar el proyecto completo**.
