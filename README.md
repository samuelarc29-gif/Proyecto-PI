# Inventech · Sistema de Gestión de Activos Tecnológicos

Aplicación de escritorio (Java 17 + Swing + JDBC + MySQL) que implementa el
proyecto integrador **Inventech** bajo arquitectura MVC + patrón DAO.

## Tecnologías

- Java 17, Swing, **FlatLaf (One Dark)** look & feel moderno
- Maven (`maven-shade-plugin` para fat jar)
- MySQL 8 + JDBC (mysql-connector-j)
- BCrypt para contraseñas
- Patrón **MVC + DAO + PreparedStatement**

## Estructura

```
src/main/java/co/inventech/
  Main.java                    ← Punto de entrada
  config/AppConfig.java        ← Configuración (config.properties / env)
  db/ConexionDB.java           ← Conexión JDBC
  util/                        ← Sesion, UITheme, PasswordUtil
  model/                       ← Entidades POJO
  dao/                         ← UsuarioDAO, ActivoDAO, TicketDAO, ...
  controller/AuthController.java
  view/
    LoginFrame.java
    MainFrame.java
    components/                ← Sidebar, StatCard, FormDialog, Tables...
    dialogs/                   ← Diálogos modales CRUD
    panels/                    ← Dashboard, Activos, Tickets, ...
sql/base_datos_gestion_activos_inventech.sql
```

## Instalación

### 1. Crear la base de datos

Abre **MySQL Workbench** y ejecuta el script:

```
sql/base_datos_gestion_activos_inventech.sql
```

Crea la base `gestion_activos_tecnologicos`, todas las tablas, catálogos
iniciales, permisos por rol y un **usuario admin demo**.

### 2. Configurar la conexión

Edita `src/main/resources/config.properties`:

```
DB_URL=jdbc:mysql://localhost:3306/gestion_activos_tecnologicos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USER=root
DB_PASS=tu_password
```

### 3. Compilar y ejecutar

```bash
mvn clean package
java -jar target/inventech-desktop.jar
```

O desde VS Code / IntelliJ: ejecutar la clase `co.inventech.Main`.

## Credenciales de prueba

| Usuario | Contraseña | Rol                        |
| ------- | ---------- | -------------------------- |
| admin   | admin123   | Administrador del sistema  |

> La primera vez la contraseña se guarda en texto plano (compatibilidad demo).
> Los usuarios creados desde la app se guardan con **BCrypt**.

## Módulos implementados

- **Dashboard** con KPIs (activos, tickets, mantenimientos, licencias) y
  gráficos de barras por área y por estado.
- **Activos**: CRUD completo, búsqueda, cambio de estado con historial.
- **Mesa de ayuda (Tickets)**: crear, asignar técnico, cambiar estado.
- **Mantenimientos**: programación preventiva y correctiva.
- **Licencias de software**: control de vencimientos y asociación a activos.
- **Proveedores**: CRUD completo.
- **ANS**: configuración de prioridades y tiempos.
- **Usuarios y roles**: CRUD con BCrypt y asignación de rol/área.
- **Reportes**: 5 reportes predefinidos con exportación CSV.

## Control de acceso

El menú lateral se filtra dinámicamente según los permisos del rol del
usuario autenticado (tabla `rol_permiso`).

## Notas

- Tema oscuro corporativo (azul + verde) inspirado en Jira / GLPI / ManageEngine.
- Todas las consultas usan `PreparedStatement` (anti-inyección SQL).
- Transacciones JDBC explícitas para asignación de técnico y cambio de estado de activo.
