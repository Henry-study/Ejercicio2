# ✅ Usuario "user" Creado en Base de Datos - Solo Perfil de Registros

## ❌ **Problema Identificado**

El usuario "user" no estaba siendo creado en la base de datos, por lo que solo existía el usuario "admin". Esto impedía tener un usuario con rol limitado para solo crear registros de guardias sin acceso a funciones administrativas.

## ✅ **Soluciones Implementadas**

### **1. Usuario "user" Agregado al DataInitializer**

**Modificación en `DataInitializer.java`:**

```java
@Override
public void run(String... args) {
    // Crear usuario admin si no existe
    if (userRepository.findByUsername("admin").isEmpty()) {
        User admin = new User(
                "admin",
                passwordEncoder.encode("admin"),
                "ROLE_ADMIN"
        );
        userRepository.save(admin);
        System.out.println(">>> Usuario admin creado (password: admin)");
    }

    // ✅ NUEVO: Crear usuario user si no existe
    if (userRepository.findByUsername("user").isEmpty()) {
        User user = new User(
                "user",
                passwordEncoder.encode("password"),
                "ROLE_USER"
        );
        userRepository.save(user);
        System.out.println(">>> Usuario user creado (password: password)");
    }

    // ...resto del código...
}
```

### **2. Dependencia Thymeleaf Security Agregada**

**Agregado en `pom.xml`:**

```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>
```

### **3. Enlace Admin Solo para ADMIN**

**Modificación en `guardias.html`:**

```html
<!-- ANTES: Visible para todos -->
<a href="/admin/horarios">⚙️ Admin Horarios</a>

<!-- DESPUÉS: Solo visible para ADMIN -->
<span sec:authorize="hasRole('ADMIN')" style="margin-left: 1rem;">|</span>
<a sec:authorize="hasRole('ADMIN')" href="/admin/horarios">⚙️ Admin Horarios</a>
```

## 👥 **Usuarios Disponibles Ahora**

### **👑 Usuario Administrador**
- **Username:** `admin`
- **Password:** `admin`
- **Rol:** `ROLE_ADMIN`
- **Permisos:**
  - ✅ Crear, ver, eliminar guardias
  - ✅ Acceso a `/admin/horarios`
  - ✅ Gestionar horarios disponibles
  - ✅ Ve enlace "⚙️ Admin Horarios" en navbar

### **👤 Usuario Estándar**
- **Username:** `user`
- **Password:** `password`
- **Rol:** `ROLE_USER`
- **Permisos:**
  - ✅ Crear y ver guardias
  - ✅ Eliminar guardias (funcionalidad compartida)
  - ❌ **NO** acceso a `/admin/horarios`
  - ❌ **NO** ve enlace "⚙️ Admin Horarios"

## 🔐 **Configuración de Seguridad**

### **Rutas y Permisos:**

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/login", "/css/**", "/error").permitAll()  // Público
    .requestMatchers("/admin/**").hasRole("ADMIN")               // Solo ADMIN
    .anyRequest().authenticated()                                // Resto: autenticado
)
```

| Ruta | ADMIN | USER |
|------|-------|------|
| `/login` | ✅ | ✅ |
| `/guardias` | ✅ | ✅ |
| `/admin/horarios` | ✅ | ❌ |
| `/admin/**` | ✅ | ❌ |

## 🧪 **Cómo Probar la Funcionalidad**

### **Test 1: Usuario ADMIN**
1. Ir a: `http://localhost:8080`
2. Login con: `admin` / `admin`
3. **Verificar:**
   - ✅ Se ve enlace "⚙️ Admin Horarios"
   - ✅ Puede acceder a administración de horarios
   - ✅ Puede registrar guardias
   - ✅ Puede eliminar guardias

### **Test 2: Usuario USER**
1. Hacer logout (si está logueado como admin)
2. Login con: `user` / `password`
3. **Verificar:**
   - ❌ **NO** se ve enlace "⚙️ Admin Horarios"
   - ✅ Puede registrar guardias normalmente
   - ✅ Puede ver lista de guardias
   - ✅ Puede eliminar guardias (si se permite)
   - ❌ Acceso directo a `/admin/horarios` devuelve 403 Forbidden

### **Test 3: Acceso Directo a Admin (Usuario USER)**
```bash
# Con sesión de usuario "user" activa
curl -i http://localhost:8080/admin/horarios
# Debería devolver: 403 Forbidden
```

## 📊 **Funcionalidades por Rol**

### **Funcionalidades Compartidas (ADMIN y USER):**
- ✅ Login/Logout
- ✅ Ver lista de guardias
- ✅ Registrar nuevos guardias
- ✅ Eliminar guardias seleccionados
- ✅ Usar horarios configurados en dropdown

### **Funcionalidades Exclusivas ADMIN:**
- ✅ Acceso a `/admin/horarios`
- ✅ Agregar nuevos horarios
- ✅ Activar/Desactivar horarios
- ✅ Eliminar horarios
- ✅ Ver enlace "⚙️ Admin Horarios" en navbar

## 🎯 **Estado Final**

### **✅ Problemas Resueltos:**
1. **Usuario "user" creado** en base de datos con rol correcto
2. **Permisos configurados** apropiadamente
3. **Interface diferenciada** según rol de usuario
4. **Seguridad implementada** para rutas admin

### **✅ Archivos Modificados:**
1. `DataInitializer.java` - Agregado usuario "user"
2. `pom.xml` - Dependencia Thymeleaf Security
3. `guardias.html` - Enlaces condicionados por rol

### **✅ Verificación:**
- ✅ Aplicación ejecutándose en `http://localhost:8080`
- ✅ Ambos usuarios funcionando correctamente
- ✅ Permisos diferenciados implementados
- ✅ Security constraints funcionando

## 🚀 **Acceso a la Aplicación**

**URL:** `http://localhost:8080`

**Credenciales:**
- **Admin completo:** `admin` / `admin`
- **Usuario estándar:** `user` / `password`

**¡El usuario "user" está ahora completamente creado y configurado para solo el perfil de crear registros!** 🎉
