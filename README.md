# Ejercicio2 — Aplicación Spring Boot con Autenticación

Aplicación web desarrollada con **Spring Boot 4.0.4** que incluye un sistema de autenticación de usuarios almacenados en una base de datos **SQLite**.

---

## Tabla de Contenidos

1. [Requisitos Previos](#requisitos-previos)
2. [Tecnologías Utilizadas](#tecnologías-utilizadas)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Configuración](#configuración)
5. [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
6. [Uso de la Aplicación](#uso-de-la-aplicación)
7. [Credenciales por Defecto](#credenciales-por-defecto)
8. [Arquitectura y Componentes](#arquitectura-y-componentes)
9. [Base de Datos](#base-de-datos)
10. [Ejecución de Tests](#ejecución-de-tests)
11. [Solución de Problemas](#solución-de-problemas)

---

## Requisitos Previos

| Requisito       | Versión mínima | Verificar instalación          |
|-----------------|----------------|-------------------------------|
| **Java JDK**    | 21             | `java -version`               |
| **Maven** *(opcional)* | 3.9+    | `mvn -version`                |

> **Nota:** No es necesario instalar Maven de forma global. El proyecto incluye el wrapper de Maven (`mvnw` / `mvnw.cmd`) que descarga automáticamente la versión correcta.

---

## Tecnologías Utilizadas

| Tecnología                  | Propósito                                      |
|-----------------------------|-------------------------------------------------|
| Spring Boot 4.0.4           | Framework principal de la aplicación            |
| Spring Security             | Autenticación y autorización                    |
| Spring Data JPA             | Persistencia y acceso a datos                   |
| Hibernate (Community Dialects) | Dialecto SQLite para JPA                     |
| SQLite                      | Base de datos embebida (archivo local)          |
| Thymeleaf                   | Motor de plantillas HTML                        |
| BCrypt                      | Encriptación de contraseñas                     |

---

## Estructura del Proyecto

```
Ejercicio2/
├── pom.xml                          # Dependencias y configuración Maven
├── mvnw / mvnw.cmd                  # Maven Wrapper (Linux/Mac y Windows)
├── ejercicio2.db                    # Base de datos SQLite (se genera al ejecutar)
├── README.md                        # Este documento
└── src/
    └── main/
        ├── java/com/example/ejercicio2/
        │   ├── Ejercicio2Application.java          # Clase principal
        │   ├── config/
        │   │   ├── SecurityConfig.java             # Configuración de Spring Security
        │   │   └── DataInitializer.java            # Creación del usuario admin inicial
        │   ├── controller/
        │   │   ├── LoginController.java            # Controlador de login y redirección
        │   │   └── GuardiaController.java          # Controlador de registro de guardias
        │   ├── model/
        │   │   ├── User.java                       # Entidad JPA de usuario
        │   │   └── Guardia.java                    # Entidad JPA de guardia
        │   ├── repository/
        │   │   ├── UserRepository.java             # Repositorio de usuarios
        │   │   └── GuardiaRepository.java          # Repositorio de guardias
        │   └── service/
        │       └── CustomUserDetailsService.java   # Servicio de autenticación
        └── resources/
            ├── application.properties              # Configuración de la aplicación
            └── templates/
                ├── login.html                      # Página de inicio de sesión
                ├── home.html                       # Redirige a /guardias
                └── guardias.html                   # Registro y listado de guardias
```

---

## Configuración

La configuración se encuentra en `src/main/resources/application.properties`:

```properties
spring.application.name=Ejercicio2

# SQLite datasource
spring.datasource.url=jdbc:sqlite:ejercicio2.db
spring.datasource.driver-class-name=org.sqlite.JDBC

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

| Propiedad | Descripción |
|-----------|-------------|
| `spring.datasource.url` | Ruta del archivo SQLite. Por defecto `ejercicio2.db` en la raíz del proyecto. |
| `spring.jpa.hibernate.ddl-auto` | `update` crea/actualiza las tablas automáticamente al iniciar. |
| `spring.jpa.show-sql` | Muestra las consultas SQL en consola (útil para depuración). |

---

## Ejecución de la Aplicación

### Opción 1: Usando Maven Wrapper (recomendado)

**En macOS / Linux:**
```bash
cd /ruta/al/proyecto/Ejercicio2
./mvnw spring-boot:run
```

**En Windows (CMD):**
```cmd
cd \ruta\al\proyecto\Ejercicio2
mvnw.cmd spring-boot:run
```

**En Windows (PowerShell):**
```powershell
cd \ruta\al\proyecto\Ejercicio2
.\mvnw.cmd spring-boot:run
```

### Opción 2: Usando Maven instalado globalmente

```bash
cd /ruta/al/proyecto/Ejercicio2
mvn spring-boot:run
```

### Opción 3: Compilar y ejecutar el JAR

```bash
# Compilar y empaquetar
./mvnw clean package -DskipTests

# Ejecutar el JAR generado
java -jar target/Ejercicio2-0.0.1-SNAPSHOT.jar
```

### Opción 4: Desde un IDE (IntelliJ IDEA, Eclipse, VS Code)

1. Importar el proyecto como proyecto **Maven**.
2. Esperar a que se descarguen las dependencias.
3. Ejecutar la clase `Ejercicio2Application.java` como aplicación Java.

---

### Verificar que la aplicación está corriendo

Una vez iniciada, se verá en la consola un mensaje similar a:

```
Started Ejercicio2Application in X.XXX seconds
>>> Usuario admin creado (password: admin)
```

La aplicación estará disponible en:

```
http://localhost:8080
```

---

## Uso de la Aplicación

### Flujo de navegación

```
┌─────────────────────────┐
│  http://localhost:8080   │
│  (redirige a /login)    │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   Página de Login       │
│   /login                │
│                         │
│   Usuario: ________     │
│   Contraseña: _____     │
│   [Entrar]              │
└───────────┬─────────────┘
            │ credenciales válidas
            ▼
┌──────────────────────────────────┐
│   Registro de Guardias           │
│   /guardias                      │
│                                  │
│   📋 Formulario:                 │
│   Nombre: ______                 │
│   Fecha: ______                  │
│   Hora de Ingreso: ______        │
│   [Registrar]                    │
│                                  │
│   📄 Tabla de Guardias:          │
│   # | Nombre | Fecha | Hora      │
│   1 | Juan   | 19/03 | 08:30    │
│   2 | María  | 19/03 | 09:00    │
│                                  │
│   [Cerrar Sesión]                │
└───────────┬──────────────────────┘
            │ cerrar sesión
            ▼
┌─────────────────────────┐
│   /login?logout         │
│   "Has cerrado sesión   │
│    correctamente"       │
└─────────────────────────┘
```

### Pasos detallados

1. **Abrir el navegador** y acceder a `http://localhost:8080`.
2. Serás redirigido automáticamente a la **página de login** (`/login`).
3. Ingresar las **credenciales** (usuario y contraseña).
4. Si las credenciales son correctas, serás redirigido a la **página de registro de guardias** (`/guardias`).
5. Si las credenciales son incorrectas, se mostrará un mensaje de error en la misma página de login.
6. En la página de guardias, completar el formulario con **nombre**, **fecha** y **hora de ingreso** del guardia y pulsar "Registrar".
7. El guardia registrado aparecerá inmediatamente en la **tabla** debajo del formulario.
8. Para **cerrar sesión**, pulsar el botón "Cerrar Sesión". Serás redirigido al login con un mensaje de confirmación.

---

## Credenciales por Defecto

Al iniciar la aplicación por primera vez, se crea automáticamente un usuario administrador:

| Campo       | Valor   |
|-------------|---------|
| **Usuario** | `admin` |
| **Contraseña** | `admin` |
| **Rol**     | `ROLE_ADMIN` |

> ⚠️ **Importante:** Se recomienda cambiar la contraseña del administrador en un entorno de producción. La contraseña se almacena encriptada con **BCrypt** en la base de datos.

---

## Arquitectura y Componentes

### Modelo (`User.java`)
Entidad JPA mapeada a la tabla `users` con los campos:
- `id` — Identificador único (generado automáticamente)
- `username` — Nombre de usuario (único, obligatorio)
- `password` — Contraseña encriptada con BCrypt (obligatoria)
- `role` — Rol del usuario, ej. `ROLE_ADMIN` (obligatorio)

### Modelo (`Guardia.java`)
Entidad JPA mapeada a la tabla `guardias` con los campos:
- `id` — Identificador único (generado automáticamente)
- `nombre` — Nombre del guardia (obligatorio)
- `fecha` — Fecha de ingreso (obligatorio, tipo `LocalDate`)
- `horaIngreso` — Hora de ingreso (obligatorio, tipo `LocalTime`)

### Repositorio (`UserRepository.java`)
Interfaz que extiende `JpaRepository` y proporciona:
- Métodos CRUD estándar (heredados)
- `findByUsername(String username)` — Busca un usuario por su nombre

### Repositorio (`GuardiaRepository.java`)
Interfaz que extiende `JpaRepository` y proporciona:
- Métodos CRUD estándar (heredados)
- `findAllByOrderByFechaDescHoraIngresoDesc()` — Lista todos los guardias ordenados por fecha y hora descendente

### Servicio (`CustomUserDetailsService.java`)
Implementa `UserDetailsService` de Spring Security. Carga los datos del usuario desde la base de datos para la autenticación.

### Configuración de Seguridad (`SecurityConfig.java`)
- Todas las rutas requieren autenticación **excepto** `/login`, `/css/**` y `/error`.
- El formulario de login es una página personalizada en `/login`.
- Tras autenticarse, se redirige a `/guardias`.
- El logout redirige a `/login?logout`.
- Codificación de contraseñas con `BCryptPasswordEncoder`.

### Inicializador de Datos (`DataInitializer.java`)
Se ejecuta al arrancar la aplicación. Si no existe un usuario `admin` en la base de datos, lo crea con contraseña `admin` encriptada.

### Controlador (`LoginController.java`)
| Ruta           | Método | Descripción                          |
|---------------|--------|--------------------------------------|
| `/login`      | GET    | Muestra la página de login           |
| `/` o `/home` | GET    | Redirige a `/guardias`               |

### Controlador (`GuardiaController.java`)
| Ruta        | Método | Descripción                                    |
|------------|--------|-------------------------------------------------|
| `/guardias` | GET    | Muestra el formulario y la tabla de guardias    |
| `/guardias` | POST   | Registra un nuevo guardia y redirige al listado |

---

## Base de Datos

La aplicación utiliza **SQLite** como base de datos embebida. El archivo se genera automáticamente al iniciar la aplicación:

```
Ejercicio2/ejercicio2.db
```

### Tabla `users`

| Columna    | Tipo    | Restricciones         |
|------------|---------|----------------------|
| `id`       | INTEGER | PRIMARY KEY, AUTO     |
| `username` | TEXT    | NOT NULL, UNIQUE      |
| `password` | TEXT    | NOT NULL              |
| `role`     | TEXT    | NOT NULL              |

### Tabla `guardias`

| Columna        | Tipo    | Restricciones     |
|----------------|---------|-------------------|
| `id`           | INTEGER | PRIMARY KEY, AUTO  |
| `nombre`       | TEXT    | NOT NULL           |
| `fecha`        | TEXT    | NOT NULL (ISO date)|
| `hora_ingreso` | TEXT    | NOT NULL (ISO time)|

### Consultar la base de datos manualmente

Si tienes `sqlite3` instalado, puedes inspeccionar los datos:

```bash
sqlite3 ejercicio2.db

# Ver tablas
.tables

# Ver usuarios registrados
SELECT id, username, role FROM users;

# Ver guardias registrados
SELECT id, nombre, fecha, hora_ingreso FROM guardias;

# Salir
.quit
```

---

## Ejecución de Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar tests con salida detallada
./mvnw test -X
```

---

## Solución de Problemas

### La aplicación no arranca

| Problema | Solución |
|----------|----------|
| `java: error: release version 21 not supported` | Instalar JDK 21 y verificar con `java -version`. |
| `Port 8080 already in use` | Otro proceso usa el puerto. Detenerlo o cambiar el puerto añadiendo `server.port=8081` en `application.properties`. |
| Error de conexión a SQLite | Verificar que la aplicación tiene permisos de escritura en el directorio del proyecto. |

### No puedo iniciar sesión

| Problema | Solución |
|----------|----------|
| Credenciales incorrectas | Usar `admin` / `admin` (usuario / contraseña). |
| Base de datos corrupta | Eliminar el archivo `ejercicio2.db` y reiniciar la aplicación. El usuario admin se recreará automáticamente. |

### Reiniciar la base de datos

Para reiniciar la base de datos desde cero:

```bash
# Detener la aplicación (Ctrl+C)
rm ejercicio2.db
./mvnw spring-boot:run
```

La aplicación creará un nuevo archivo `ejercicio2.db` con el usuario `admin` inicial.

---

## Cambiar el Puerto de la Aplicación

Si necesitas ejecutar la aplicación en un puerto diferente, agregar en `application.properties`:

```properties
server.port=9090
```

La aplicación estará disponible en `http://localhost:9090`.

