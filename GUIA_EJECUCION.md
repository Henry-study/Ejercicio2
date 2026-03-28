# Guía de Ejecución - Sistema de Registro de Guardias

## Descripción del Proyecto
Este proyecto es un sistema web desarrollado en Spring Boot que permite la autenticación de usuarios y la gestión de guardias de seguridad. Los usuarios pueden registrar nuevos guardias con información de fecha, hora de ingreso y eliminar registros existentes.

## Requisitos Previos
- Java 11 o superior
- Maven (se incluye Maven Wrapper para facilitar la ejecución)

## Cómo Ejecutar la Aplicación

### Opción 1: Usando Maven Wrapper (Recomendado)
```bash
# Navegar al directorio del proyecto
cd /ruta/al/proyecto/Ejercicio2

# Ejecutar la aplicación
./mvnw spring-boot:run
```

### Opción 2: Usando Maven (si está instalado)
```bash
# Navegar al directorio del proyecto
cd /ruta/al/proyecto/Ejercicio2

# Ejecutar la aplicación
mvn spring-boot:run
```

## Acceso a la Aplicación
Una vez que la aplicación esté ejecutándose:
1. Abrir un navegador web
2. Navegar a: `http://localhost:8080`
3. La aplicación redirigirá automáticamente al formulario de login

## Usuarios de Prueba
La aplicación incluye usuarios predefinidos en la base de datos SQLite:

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin   | password   | ADMIN |
| user    | password   | USER |

## Funcionalidades de la Aplicación

### 1. Autenticación
- **Página de login**: `/login`
- Validación contra base de datos SQLite
- Sesión persistente hasta cerrar sesión

### 2. Gestión de Guardias
Una vez autenticado, el usuario accede a la página principal donde puede:

#### Registrar Nuevos Guardias
- **Nombre del Guardia**: Campo obligatorio para el nombre completo
- **Fecha**: Selector de fecha (formato dd/MM/yyyy)
- **Hora de Ingreso**: Selector de hora (formato HH:mm)

#### Visualizar Guardias Registrados
- Tabla con todos los guardias registrados
- Ordenados por fecha y hora de ingreso (más recientes primero)
- Formato de fecha: dd/MM/yyyy
- Formato de hora: HH:mm

#### **NUEVA FUNCIONALIDAD: Eliminación Múltiple**
- **Seleccionar guardias**: Checkbox individual para cada guardia
- **Seleccionar todos**: Checkbox principal que selecciona/deselecciona todos los guardias
- **Botón Eliminar**: 
  - Se habilita solo cuando hay guardias seleccionados
  - Muestra diálogo de confirmación antes de eliminar
  - Cuenta la cantidad de guardias a eliminar
- **Mensajes de confirmación**: 
  - Confirma la cantidad de registros eliminados
  - Mensaje de error si no se selecciona ningún guardia

### 3. Seguridad
- Autenticación requerida para todas las páginas excepto `/login`
- Protección CSRF habilitada
- Sesiones seguras

## Estructura de Archivos Importantes

```
src/
├── main/
│   ├── java/com/example/ejercicio2/
│   │   ├── controller/
│   │   │   ├── GuardiaController.java    # Gestión de guardias y eliminación
│   │   │   └── LoginController.java      # Autenticación
│   │   ├── model/
│   │   │   ├── Guardia.java             # Entidad Guardia
│   │   │   └── User.java                # Entidad Usuario
│   │   ├── repository/
│   │   │   ├── GuardiaRepository.java   # Operaciones BD guardias
│   │   │   └── UserRepository.java      # Operaciones BD usuarios
│   │   ├── service/
│   │   │   └── CustomUserDetailsService.java  # Servicio de autenticación
│   │   └── config/
│   │       ├── SecurityConfig.java      # Configuración de seguridad
│   │       └── DataInitializer.java     # Datos iniciales
│   └── resources/
│       ├── application.properties       # Configuración de la aplicación
│       └── templates/
│           ├── login.html              # Página de login
│           ├── home.html               # Página principal
│           └── guardias.html           # Gestión de guardias (con nueva funcionalidad)
└── ejercicio2.db                       # Base de datos SQLite
```

## Funcionalidad del CustomUserDetailsService

El `CustomUserDetailsService` es un componente clave del sistema de autenticación que cumple las siguientes funciones:

### Propósito Principal
- **Integración con Spring Security**: Implementa la interfaz `UserDetailsService` para proporcionar a Spring Security la información necesaria sobre los usuarios

### Funciones Específicas

1. **Carga de Usuario por Username**:
   - Método: `loadUserByUsername(String username)`
   - Busca el usuario en la base de datos por su nombre de usuario
   - Lanza `UsernameNotFoundException` si no encuentra el usuario

2. **Conversión a UserDetails**:
   - Convierte la entidad `User` del modelo de datos a un objeto `UserDetails` que Spring Security puede entender
   - Incluye: username, password, authorities (roles)

3. **Gestión de Roles y Permisos**:
   - Asigna las autoridades correspondientes basadas en el campo `role` de la entidad User
   - Formato: `ROLE_` + nombre del rol (ej: `ROLE_ADMIN`, `ROLE_USER`)

4. **Configuración de Estado de Cuenta**:
   - `accountNonExpired`: true (cuenta no expirada)
   - `accountNonLocked`: true (cuenta no bloqueada)
   - `credentialsNonExpired`: true (credenciales no expiradas)
   - `enabled`: true (cuenta habilitada)

### Código de Referencia
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities("ROLE_" + user.getRole())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
```

## Base de Datos
- **Tipo**: SQLite
- **Archivo**: `ejercicio2.db` (se crea automáticamente)
- **Tablas**: `users`, `guardias`
- **Inicialización**: Automática con datos de prueba

## Puertos y URLs
- **Puerto por defecto**: 8080
- **URL principal**: http://localhost:8080
- **Login**: http://localhost:8080/login
- **Gestión de guardias**: http://localhost:8080/guardias

## Comandos para Compilación y Testing

### Compilar el proyecto
```bash
./mvnw clean compile
```

### Ejecutar tests
```bash
./mvnw test
```

### Generar JAR ejecutable
```bash
./mvnw clean package
```

### Ejecutar JAR generado
```bash
java -jar target/Ejercicio2-0.0.1-SNAPSHOT.jar
```

## Comandos Git para Subir al Repositorio

Para subir este proyecto al repositorio GitHub (https://github.com/Henry-studyque):

### Configuración Inicial (solo una vez)
```bash
# Inicializar repositorio git (si no existe)
git init

# Agregar el repositorio remoto
git remote add origin https://github.com/Henry-studyque/nombre-del-repositorio.git

# Configurar usuario (si no está configurado globalmente)
git config user.name "Tu Nombre"
git config user.email "tu-email@example.com"
```

### Comandos para Push
```bash
# 1. Agregar todos los archivos al staging area
git add .

# 2. Crear commit con mensaje descriptivo
git commit -m "Implementada funcionalidad de eliminación múltiple de guardias"

# 3. Subir cambios a GitHub (primera vez)
git push -u origin main

# Para pushes posteriores
git push
```

### Comandos Adicionales Útiles
```bash
# Ver estado de los archivos
git status

# Ver historial de commits
git log --oneline

# Crear nueva rama
git checkout -b nueva-funcionalidad

# Cambiar entre ramas
git checkout main
git checkout nombre-rama
```

## Solución de Problemas

### La aplicación no inicia
1. Verificar que Java esté instalado: `java -version`
2. Verificar que el puerto 8080 no esté ocupado
3. Revisar logs de consola para errores específicos

### Error de conexión a base de datos
1. Verificar que el archivo `ejercicio2.db` se haya creado
2. Verificar permisos de escritura en el directorio
3. Revisar configuración en `application.properties`

### Problemas de autenticación
1. Verificar que los usuarios de prueba estén en la base de datos
2. Usar las credenciales exactas: admin/password o user/password
3. Verificar que las cookies estén habilitadas en el navegador

## Notas de Desarrollo

- La aplicación usa **Spring Boot DevTools** para hot-reload durante el desarrollo
- La base de datos se inicializa automáticamente con datos de prueba
- Los passwords están encriptados usando BCrypt
- El frontend usa **Thymeleaf** como motor de plantillas
- Los estilos CSS están embebidos en las plantillas HTML
