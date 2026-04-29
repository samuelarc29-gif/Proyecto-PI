CREATE DATABASE gestion_activos_tecnologicos;
USE gestion_activos_tecnologicos;

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE permiso (
    id_permiso INT AUTO_INCREMENT PRIMARY KEY,
    nombre_permiso VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE rol_permiso (
    id_rol_permiso INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso)
);

CREATE TABLE area (
    id_area INT AUTO_INCREMENT PRIMARY KEY,
    nombre_area VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    id_area INT,
    nombre_completo VARCHAR(150) NOT NULL,
    documento VARCHAR(50) NOT NULL UNIQUE,
    cargo VARCHAR(100),
    correo VARCHAR(150) UNIQUE,
    telefono VARCHAR(50),
    estado_usuario VARCHAR(50) DEFAULT 'Activo',
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_area) REFERENCES area(id_area)
);

CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_proveedor VARCHAR(150) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(50),
    correo VARCHAR(150),
    direccion VARCHAR(200),
    descripcion_soporte VARCHAR(255)
);

CREATE TABLE estado_activo (
    id_estado_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE tipo_activo (
    id_tipo_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE marca (
    id_marca INT AUTO_INCREMENT PRIMARY KEY,
    nombre_marca VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE activo (
    id_activo INT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_activo INT NOT NULL,
    id_marca INT NOT NULL,
    id_proveedor INT,
    id_estado_activo INT NOT NULL,
    id_area INT,
    modelo VARCHAR(100),
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    codigo_interno VARCHAR(100) NOT NULL UNIQUE,
    fecha_adquisicion DATE,
    fecha_garantia DATE,
    observaciones TEXT,
    FOREIGN KEY (id_tipo_activo) REFERENCES tipo_activo(id_tipo_activo),
    FOREIGN KEY (id_marca) REFERENCES marca(id_marca),
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
    FOREIGN KEY (id_estado_activo) REFERENCES estado_activo(id_estado_activo),
    FOREIGN KEY (id_area) REFERENCES area(id_area)
);

CREATE TABLE asignacion_activo (
    id_asignacion_activo INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha_asignacion DATE NOT NULL,
    fecha_devolucion DATE,
    observaciones TEXT,
    estado_asignacion VARCHAR(50) DEFAULT 'Activa',
    FOREIGN KEY (id_activo) REFERENCES activo(id_activo),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE tipo_mantenimiento (
    id_tipo_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE estado_ticket (
    id_estado_ticket INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE ans (
    id_ans INT AUTO_INCREMENT PRIMARY KEY,
    tipo_solicitud VARCHAR(100) NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    tiempo_respuesta_horas INT NOT NULL,
    tiempo_solucion_horas INT NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE ticket (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_usuario_solicitante INT NOT NULL,
    id_estado_ticket INT NOT NULL,
    id_ans INT,
    descripcion_falla TEXT NOT NULL,
    fecha_reporte DATETIME DEFAULT CURRENT_TIMESTAMP,
    prioridad VARCHAR(50),
    fecha_cierre_tecnico DATETIME,
    fecha_cierre_usuario DATETIME,
    observacion_cierre_usuario TEXT,
    FOREIGN KEY (id_activo) REFERENCES activo(id_activo),
    FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_estado_ticket) REFERENCES estado_ticket(id_estado_ticket),
    FOREIGN KEY (id_ans) REFERENCES ans(id_ans)
);

CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_tipo_mantenimiento INT NOT NULL,
    fecha_programada DATE,
    observaciones TEXT,
    FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket),
    FOREIGN KEY (id_tipo_mantenimiento) REFERENCES tipo_mantenimiento(id_tipo_mantenimiento)
);

CREATE TABLE asignacion_tecnico (
    id_asignacion_tecnico INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario_tecnico INT NOT NULL,
    fecha_asignacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_atencion DATETIME,
    diagnostico TEXT,
    solucion_aplicada TEXT,
    observaciones TEXT,
    cierre_tecnico TEXT,
    FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket),
    FOREIGN KEY (id_usuario_tecnico) REFERENCES usuario(id_usuario)
);

CREATE TABLE licencia_software (
    id_licencia INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    nombre_software VARCHAR(150) NOT NULL,
    tipo_licencia VARCHAR(100),
    codigo_licencia VARCHAR(150) UNIQUE,
    fecha_adquisicion DATE,
    fecha_vencimiento DATE,
    estado_licencia VARCHAR(50) DEFAULT 'Activa',
    observaciones TEXT,
    FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
);

INSERT INTO rol (nombre_rol, descripcion) VALUES
('Administrador del sistema', 'Acceso total al sistema'),
('Responsable de área', 'Gestión de activos dentro de su área'),
('Usuario general', 'Usuario final'),
('Proveedor', 'Actor externo'),
('Coordinador de soporte técnico', 'Gestiona soporte'),
('Técnico de soporte', 'Atiende tickets');

INSERT INTO estado_activo (nombre_estado, descripcion) VALUES
('Disponible', 'Activo disponible'),
('Asignado', 'Activo asignado'),
('En mantenimiento', 'Activo en mantenimiento'),
('Dado de baja', 'Activo fuera de servicio');

INSERT INTO tipo_activo (nombre_tipo, descripcion) VALUES
('PC de escritorio', 'Computador de escritorio'),
('Portátil', 'Computador portátil'),
('Impresora', 'Equipo de impresión'),
('Monitor', 'Pantalla'),
('Router', 'Equipo de red');

INSERT INTO marca (nombre_marca, descripcion) VALUES
('HP', 'Marca tecnológica'),
('Dell', 'Marca tecnológica'),
('Lenovo', 'Marca tecnológica'),
('Asus', 'Marca tecnológica'),
('Epson', 'Marca tecnológica');

INSERT INTO tipo_mantenimiento (nombre_tipo, descripcion) VALUES
('Preventivo', 'Mantenimiento preventivo'),
('Correctivo', 'Mantenimiento correctivo'),
('Actualización', 'Actualización tecnológica');

INSERT INTO estado_ticket (nombre_estado, descripcion) VALUES
('Abierto', 'Ticket creado'),
('Asignado', 'Ticket asignado'),
('En proceso', 'Ticket en atención'),
('Cerrado por técnico', 'Solucionado técnicamente'),
('Cerrado por usuario', 'Validado por usuario');

INSERT INTO area (nombre_area, descripcion) VALUES
('Sistemas', 'Área de tecnología'),
('Administrativa', 'Área administrativa'),
('Contabilidad', 'Área contable'),
('Recursos Humanos', 'Área de personal');

INSERT INTO ans (tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion) VALUES
('Soporte técnico', 'Alta', 2, 8, 'Atención prioritaria'),
('Soporte técnico', 'Media', 4, 24, 'Atención normal'),
('Soporte técnico', 'Baja', 8, 48, 'Atención baja prioridad');

INSERT INTO usuario (
id_rol,
id_area,
nombre_completo,
documento,
cargo,
correo,
telefono,
estado_usuario
)
VALUES (
1,
1,
'Administrador General',
'123456789',
'Administrador',
'admin@inventech.com',
'3001234567',
'Activo'
);