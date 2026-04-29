# Inventech - Sistema de Gestión de Activos Tecnológicos

Aplicación de escritorio en **Java Swing** con arquitectura **MVC**, conexión **JDBC a MySQL** y tema oscuro con acentos verde/azul (FlatLaf).

## 📁 Estructura

```
inventech/
├── pom.xml
├── sql/
│   └── gestion_activos_tecnologicos.sql   ← Script de BD
└── src/main/
    ├── resources/
    │   └── db.properties                   ← Configuración de conexión
    └── java/com/inventech/
        ├── Main.java
        ├── config/        → ConexionBD (JDBC centralizado)
        ├── model/         → POJOs (Usuario, Activo, Marca, ...)
        ├── dao/           → Acceso a datos (UsuarioDAO, ActivoDAO, ...)
        ├── controller/    → LoginController, ...
        ├── view/          → LoginView, DashboardView, MenuPorRol
        │   └── crud/      → UsuarioView, ActivoView, MarcaView, ProveedorView
        └── util/          → Sesion, Tema (paleta de colores)
```

## ✅ Requisitos previos

1. **JDK 17** o superior.
2. **Maven 3.8+**.
3. **MySQL 8** con la BD `gestion_activos_tecnologicos` creada (usar `sql/gestion_activos_tecnologicos.sql`).
4. **VS Code** con las extensiones:
   - *Extension Pack for Java* (Microsoft)
   - *Maven for Java*

## 🚀 Pasos para abrir en Visual Studio Code

1. Abre VS Code → `File` → `Open Folder...` → selecciona la carpeta `inventech/`.
2. Espera a que VS Code descargue las dependencias de Maven (esquina inferior derecha).
3. Abre MySQL Workbench y ejecuta el script `sql/gestion_activos_tecnologicos.sql`.
4. Edita `src/main/resources/db.properties` con tus credenciales:
   ```properties
   db.url=jdbc:mysql://localhost:3306/gestion_activos_tecnologicos?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
   db.user=root
   db.password=TU_PASSWORD
   ```
5. Ejecuta `Main.java` (botón ▶ "Run") o desde terminal:
   ```bash
   mvn clean package
   java -jar target/inventech-jar-with-dependencies.jar
   ```

## 🔐 Login

El sistema autentica por **correo o documento** consultando la tabla `usuario` (estado = 'Activo'). 

> **Importante**: el esquema entregado **no incluye columna de contraseña**. Para uso real, agrega:
> ```sql
> ALTER TABLE usuario ADD COLUMN password_hash VARCHAR(255);
> ```
> Y valida con BCrypt (`org.mindrot:jbcrypt`) en `LoginController`.

Para probar, primero inserta usuarios con cada rol, por ejemplo desde Workbench:
```sql
INSERT INTO usuario (id_rol, id_area, nombre_completo, documento, cargo, correo, telefono, estado_usuario)
VALUES
((SELECT id_rol FROM rol WHERE nombre_rol='Administrador del sistema'), 1, 'Admin Inventech', '1000', 'Admin', 'admin@inventech.co', '3000000000', 'Activo'),
((SELECT id_rol FROM rol WHERE nombre_rol='Coordinador de soporte técnico'), 1, 'Coord Soporte', '2000', 'Coord', 'coord@inventech.co', '3000000001', 'Activo'),
((SELECT id_rol FROM rol WHERE nombre_rol='Técnico de soporte'), 1, 'Tec Uno', '3000', 'Técnico', 'tec1@inventech.co', '3000000002', 'Activo');
```

## 🧩 Módulos incluidos

| Módulo | Estado |
|---|---|
| Login con identificación de rol | ✅ Implementado |
| Dashboard con métricas reales | ✅ Implementado (consulta BD) |
| CRUD Usuarios | ✅ Implementado |
| CRUD Activos | ✅ Implementado |
| CRUD Marcas | ✅ Implementado |
| CRUD Proveedores | ✅ Implementado |
| Asignación de activos | 🟡 DAO listo (`AsignacionActivoDAO`), falta vista |
| Tickets, Mantenimientos, Licencias, ANS, Reportes | 🟡 Modelo creado, replica el patrón de los CRUD existentes |

> Los CRUD de Tickets, Licencias, Mantenimientos y ANS siguen exactamente el mismo patrón de `MarcaView` / `ActivoView`. Crea una nueva clase en `view/crud/`, un `XxxDAO` en `dao/`, y agrégalos al `MenuPorRol`.

## 🎨 Permisos por rol

Definidos en `view/MenuPorRol.java`. El menú lateral del dashboard cambia según el `nombre_rol` del usuario logueado.

## 🛠️ Reglas de arquitectura

- ❌ **Nunca** datos quemados ni listas en memoria.
- ✅ Todos los `JComboBox` se llenan con DAO desde MySQL.
- ✅ Todas las `JTable` muestran datos reales con joins.
- ✅ Conexión centralizada en `config.ConexionBD`.
- ✅ Botones Guardar / Actualizar / Eliminar / Limpiar en cada CRUD.

## 📦 Dependencias (pom.xml)

- `com.mysql:mysql-connector-j:8.3.0` — driver JDBC
- `com.formdev:flatlaf:3.4` — Look & Feel oscuro moderno
