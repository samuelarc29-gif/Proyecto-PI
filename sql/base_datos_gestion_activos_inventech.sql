CREATE DATABASE gestion_activos_tecnologicos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE gestion_activos_tecnologicos;

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE permiso (
    id_permiso INT AUTO_INCREMENT PRIMARY KEY,
    nombre_permiso VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE rol_permiso (
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    PRIMARY KEY (id_rol, id_permiso),
    CONSTRAINT fk_rolpermiso_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rolpermiso_permiso FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE area (
    id_area INT AUTO_INCREMENT PRIMARY KEY,
    nombre_area VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado_area ENUM('Activa','Inactiva') NOT NULL DEFAULT 'Activa'
);

CREATE TABLE tipo_activo (
    id_tipo_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE marca (
    id_marca INT AUTO_INCREMENT PRIMARY KEY,
    nombre_marca VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE estado_activo (
    id_estado_activo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE tipo_mantenimiento (
    id_tipo_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE estado_ticket (
    id_estado_ticket INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_proveedor VARCHAR(120) NOT NULL,
    contacto VARCHAR(120),
    telefono VARCHAR(30),
    correo VARCHAR(120) UNIQUE,
    direccion VARCHAR(180),
    descripcion_soporte TEXT,
    estado_proveedor ENUM('Activo','Inactivo') NOT NULL DEFAULT 'Activo'
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    id_area INT,
    nombre_completo VARCHAR(150) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    cargo VARCHAR(100),
    correo VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    nombre_usuario VARCHAR(80) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    estado_usuario ENUM('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_usuario_area FOREIGN KEY (id_area) REFERENCES area(id_area)
        ON UPDATE CASCADE ON DELETE SET NULL
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
    codigo_interno VARCHAR(80) NOT NULL UNIQUE,
    fecha_adquisicion DATE,
    fecha_garantia DATE,
    observaciones TEXT,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activo_tipo FOREIGN KEY (id_tipo_activo) REFERENCES tipo_activo(id_tipo_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_activo_marca FOREIGN KEY (id_marca) REFERENCES marca(id_marca)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_activo_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_activo_estado FOREIGN KEY (id_estado_activo) REFERENCES estado_activo(id_estado_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_activo_area FOREIGN KEY (id_area) REFERENCES area(id_area)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE asignacion_activo (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_usuario_responsable INT NOT NULL,
    id_area INT,
    fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion DATETIME,
    observaciones TEXT,
    estado_asignacion ENUM('Activa','Finalizada') NOT NULL DEFAULT 'Activa',
    CONSTRAINT fk_asignacion_activo FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_usuario FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_area FOREIGN KEY (id_area) REFERENCES area(id_area)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE historial_estado_activo (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_estado_anterior INT,
    id_estado_nuevo INT NOT NULL,
    id_usuario_registra INT,
    fecha_cambio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT,
    CONSTRAINT fk_historial_activo FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_historial_estado_anterior FOREIGN KEY (id_estado_anterior) REFERENCES estado_activo(id_estado_activo)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_historial_estado_nuevo FOREIGN KEY (id_estado_nuevo) REFERENCES estado_activo(id_estado_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (id_usuario_registra) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE ans (
    id_ans INT AUTO_INCREMENT PRIMARY KEY,
    tipo_solicitud VARCHAR(100) NOT NULL,
    prioridad ENUM('Baja','Media','Alta','Crítica') NOT NULL,
    tiempo_respuesta_horas INT NOT NULL,
    tiempo_solucion_horas INT NOT NULL,
    estado_ans ENUM('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
    CONSTRAINT chk_ans_tiempos CHECK (tiempo_respuesta_horas > 0 AND tiempo_solucion_horas > 0)
);

CREATE TABLE ticket (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_usuario_solicitante INT NOT NULL,
    id_estado_ticket INT NOT NULL,
    id_ans INT,
    descripcion_falla TEXT NOT NULL,
    fecha_reporte DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prioridad ENUM('Baja','Media','Alta','Crítica') NOT NULL DEFAULT 'Media',
    fecha_cierre_usuario DATETIME,
    observacion_cierre TEXT,
    CONSTRAINT fk_ticket_activo FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_ticket_usuario FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_ticket_estado FOREIGN KEY (id_estado_ticket) REFERENCES estado_ticket(id_estado_ticket)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_ticket_ans FOREIGN KEY (id_ans) REFERENCES ans(id_ans)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT NOT NULL,
    id_tipo_mantenimiento INT NOT NULL,
    id_ticket INT,
    id_usuario_solicitante INT,
    fecha_programada DATE,
    fecha_realizacion DATETIME,
    descripcion TEXT,
    prioridad ENUM('Baja','Media','Alta','Crítica') DEFAULT 'Media',
    estado_mantenimiento ENUM('Programado','En proceso','Finalizado','Cancelado') NOT NULL DEFAULT 'Programado',
    CONSTRAINT fk_mantenimiento_activo FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_mantenimiento_tipo FOREIGN KEY (id_tipo_mantenimiento) REFERENCES tipo_mantenimiento(id_tipo_mantenimiento)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_mantenimiento_ticket FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_mantenimiento_usuario FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE asignacion_tecnico (
    id_asignacion_tecnico INT AUTO_INCREMENT PRIMARY KEY,
    id_ticket INT NOT NULL,
    id_usuario_tecnico INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    diagnostico TEXT,
    solucion_aplicada TEXT,
    fecha_cierre_tecnico DATETIME,
    observaciones TEXT,
    CONSTRAINT fk_asigtec_ticket FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_asigtec_usuario FOREIGN KEY (id_usuario_tecnico) REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE licencia_software (
    id_licencia INT AUTO_INCREMENT PRIMARY KEY,
    id_activo INT,
    nombre_software VARCHAR(120) NOT NULL,
    tipo_licencia VARCHAR(80) NOT NULL,
    codigo_licencia VARCHAR(120) NOT NULL UNIQUE,
    fecha_adquisicion DATE,
    fecha_vencimiento DATE,
    estado_licencia ENUM('Activa','Vencida','Suspendida') NOT NULL DEFAULT 'Activa',
    observaciones TEXT,
    CONSTRAINT fk_licencia_activo FOREIGN KEY (id_activo) REFERENCES activo(id_activo)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE INDEX idx_activo_area ON activo(id_area);
CREATE INDEX idx_activo_estado ON activo(id_estado_activo);
CREATE INDEX idx_ticket_estado ON ticket(id_estado_ticket);
CREATE INDEX idx_ticket_prioridad ON ticket(prioridad);
CREATE INDEX idx_ticket_fecha ON ticket(fecha_reporte);
CREATE INDEX idx_asignacion_activa ON asignacion_activo(id_activo, estado_asignacion);


INSERT INTO rol (nombre_rol, descripcion) VALUES
('Administrador del sistema', 'Control total sobre usuarios, activos, proveedores, reportes y configuración.'),
('Responsable de área', 'Gestiona activos dentro de su área organizacional.'),
('Usuario general', 'Usuario final que consulta activos asignados y reporta novedades.'),
('Proveedor', 'Actor externo que consulta activos suministrados y actualiza soporte.'),
('Coordinador de soporte técnico', 'Gestiona, prioriza y asigna solicitudes de soporte.'),
('Técnico de soporte', 'Atiende tickets asignados y registra diagnóstico y solución.');

INSERT INTO area (nombre_area, descripcion) VALUES
('Sistemas', 'Área de tecnología e infraestructura'),
('Administración', 'Área administrativa'),
('Contabilidad', 'Área financiera y contable'),
('Talento Humano', 'Área de gestión humana');

INSERT INTO tipo_activo (nombre_tipo, descripcion) VALUES
('Computador de escritorio', 'Equipo de cómputo fijo'),
('Portátil', 'Equipo de cómputo portátil'),
('Impresora', 'Equipo de impresión'),
('Servidor', 'Servidor físico o virtual'),
('Software', 'Activo lógico o licencia de software'),
('Dispositivo de red', 'Router, switch, access point u otro dispositivo de red');

INSERT INTO marca (nombre_marca) VALUES
('HP'), ('Dell'), ('Lenovo'), ('Asus'), ('Cisco'), ('Microsoft'), ('Epson');

INSERT INTO estado_activo (nombre_estado, descripcion) VALUES
('Disponible', 'Activo disponible para asignación'),
('Asignado', 'Activo asignado a un usuario o área'),
('En mantenimiento', 'Activo en revisión o reparación'),
('Dado de baja', 'Activo retirado del inventario'),
('En garantía', 'Activo en proceso asociado a garantía');

INSERT INTO tipo_mantenimiento (nombre_tipo, descripcion) VALUES
('Preventivo', 'Mantenimiento programado para evitar fallas'),
('Correctivo', 'Mantenimiento realizado por falla o incidente');

INSERT INTO estado_ticket (nombre_estado, descripcion) VALUES
('Abierto', 'Ticket creado por el usuario'),
('Asignado', 'Ticket asignado a uno o varios técnicos'),
('En proceso', 'Ticket en atención técnica'),
('Cerrado técnico', 'Caso cerrado por técnico, pendiente de validación'),
('Cerrado usuario', 'Caso validado y cerrado por el usuario'),
('Cancelado', 'Ticket cancelado');

INSERT INTO ans (tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas) VALUES
('Soporte técnico', 'Crítica', 1, 4),
('Soporte técnico', 'Alta', 2, 8),
('Soporte técnico', 'Media', 4, 24),
('Soporte técnico', 'Baja', 8, 48);

INSERT INTO permiso (nombre_permiso, descripcion) VALUES
('GESTIONAR_USUARIOS', 'Crear, consultar, actualizar y eliminar usuarios'),
('GESTIONAR_ACTIVOS', 'Administrar activos tecnológicos'),
('ASIGNAR_ACTIVOS', 'Asignar y reasignar activos'),
('GESTIONAR_MANTENIMIENTOS', 'Registrar y actualizar mantenimientos'),
('GESTIONAR_TICKETS', 'Registrar, asignar y cerrar tickets'),
('GESTIONAR_PROVEEDORES', 'Administrar proveedores'),
('GESTIONAR_AREAS', 'Administrar áreas organizacionales'),
('GENERAR_REPORTES', 'Generar reportes del sistema'),
('GESTIONAR_ANS', 'Configurar acuerdos de nivel de servicio'),
('GESTIONAR_LICENCIAS', 'Administrar licencias de software');

-- Permisos básicos por rol
INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol r
JOIN permiso p
WHERE r.nombre_rol = 'Administrador del sistema';

INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol r
JOIN permiso p ON p.nombre_permiso IN ('GESTIONAR_ACTIVOS','ASIGNAR_ACTIVOS','GENERAR_REPORTES')
WHERE r.nombre_rol = 'Responsable de área';

INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol r
JOIN permiso p ON p.nombre_permiso IN ('GESTIONAR_TICKETS')
WHERE r.nombre_rol = 'Usuario general';

INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol r
JOIN permiso p ON p.nombre_permiso IN ('GESTIONAR_TICKETS','GESTIONAR_MANTENIMIENTOS','GESTIONAR_ANS')
WHERE r.nombre_rol = 'Coordinador de soporte técnico';

INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol r
JOIN permiso p ON p.nombre_permiso IN ('GESTIONAR_TICKETS','GESTIONAR_MANTENIMIENTOS')
WHERE r.nombre_rol = 'Técnico de soporte';

-- Usuarios de prueba. Nota: para producción, guardar contraseñas con hash desde Java.
INSERT INTO usuario (id_rol, id_area, nombre_completo, documento, cargo, correo, telefono, nombre_usuario, contrasena_hash)
VALUES
((SELECT id_rol FROM rol WHERE nombre_rol='Administrador del sistema'), 1, 'Administrador Inventech', '1000', 'Administrador', 'admin@inventech.com', '3000000000', 'admin', SHA2('admin123', 256)),
((SELECT id_rol FROM rol WHERE nombre_rol='Coordinador de soporte técnico'), 1, 'Laura Coordinadora', '1001', 'Coordinadora de soporte', 'laura@inventech.com', '3001110000', 'lcoordinadora', SHA2('coord123', 256)),
((SELECT id_rol FROM rol WHERE nombre_rol='Técnico de soporte'), 1, 'Carlos Técnico', '9001', 'Técnico', 'carlos@inventech.com', '3001111111', 'ctecnico', SHA2('tec123', 256)),
((SELECT id_rol FROM rol WHERE nombre_rol='Técnico de soporte'), 1, 'Ana Técnica', '9002', 'Técnica', 'ana@inventech.com', '3002222222', 'atecnica', SHA2('tec123', 256)),
((SELECT id_rol FROM rol WHERE nombre_rol='Usuario general'), 2, 'Juan Usuario', '9003', 'Auxiliar administrativo', 'juan@inventech.com', '3003333333', 'jusuario', SHA2('user123', 256));

INSERT INTO proveedor (nombre_proveedor, contacto, telefono, correo, direccion, descripcion_soporte)
VALUES ('Proveedor Prueba', 'Soporte Técnico', '6021234567', 'soporte@proveedor.com', 'Cali', 'Soporte general y garantías');

INSERT INTO activo (id_tipo_activo, id_marca, id_proveedor, id_estado_activo, id_area, modelo, numero_serie, codigo_interno, fecha_adquisicion, fecha_garantia, observaciones)
VALUES
((SELECT id_tipo_activo FROM tipo_activo WHERE nombre_tipo='Portátil'), (SELECT id_marca FROM marca WHERE nombre_marca='HP'), 1, (SELECT id_estado_activo FROM estado_activo WHERE nombre_estado='Disponible'), 2, 'HP ProBook', 'SERIE-001', 'ACT-001', '2025-01-10', '2026-01-10', 'Activo de prueba'),
((SELECT id_tipo_activo FROM tipo_activo WHERE nombre_tipo='Computador de escritorio'), (SELECT id_marca FROM marca WHERE nombre_marca='Dell'), 1, (SELECT id_estado_activo FROM estado_activo WHERE nombre_estado='Disponible'), 3, 'Dell Optiplex', 'SERIE-002', 'ACT-002', '2025-02-15', '2026-02-15', 'Activo de prueba');

DELIMITER //

CREATE PROCEDURE sp_login_usuario(
    IN p_nombre_usuario VARCHAR(80),
    IN p_contrasena VARCHAR(255)
)
BEGIN
    SELECT 
        u.id_usuario,
        u.nombre_completo,
        u.nombre_usuario,
        u.correo,
        r.nombre_rol,
        a.nombre_area,
        u.estado_usuario
    FROM usuario u
    INNER JOIN rol r ON u.id_rol = r.id_rol
    LEFT JOIN area a ON u.id_area = a.id_area
    WHERE u.nombre_usuario = p_nombre_usuario
      AND u.contrasena_hash = SHA2(p_contrasena, 256)
      AND u.estado_usuario = 'Activo';
END //

CREATE PROCEDURE sp_registrar_activo(
    IN p_id_tipo_activo INT,
    IN p_id_marca INT,
    IN p_id_proveedor INT,
    IN p_id_area INT,
    IN p_modelo VARCHAR(100),
    IN p_numero_serie VARCHAR(100),
    IN p_codigo_interno VARCHAR(80),
    IN p_fecha_adquisicion DATE,
    IN p_fecha_garantia DATE,
    IN p_observaciones TEXT
)
BEGIN
    IF EXISTS (SELECT 1 FROM activo WHERE numero_serie = p_numero_serie OR codigo_interno = p_codigo_interno) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El activo ya existe: número de serie o código interno duplicado.';
    ELSE
        INSERT INTO activo (
            id_tipo_activo, id_marca, id_proveedor, id_estado_activo, id_area,
            modelo, numero_serie, codigo_interno, fecha_adquisicion, fecha_garantia, observaciones
        ) VALUES (
            p_id_tipo_activo, p_id_marca, p_id_proveedor,
            (SELECT id_estado_activo FROM estado_activo WHERE nombre_estado = 'Disponible'),
            p_id_area, p_modelo, p_numero_serie, p_codigo_interno,
            p_fecha_adquisicion, p_fecha_garantia, p_observaciones
        );
    END IF;
END //

CREATE PROCEDURE sp_asignar_activo(
    IN p_id_activo INT,
    IN p_id_usuario_responsable INT,
    IN p_observaciones TEXT
)
BEGIN
    DECLARE v_estado_actual VARCHAR(80);
    DECLARE v_id_area INT;
    DECLARE v_id_estado_asignado INT;

    SELECT ea.nombre_estado, a.id_area
    INTO v_estado_actual, v_id_area
    FROM activo a
    INNER JOIN estado_activo ea ON a.id_estado_activo = ea.id_estado_activo
    WHERE a.id_activo = p_id_activo;

    IF v_estado_actual IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El activo no existe.';
    ELSEIF v_estado_actual <> 'Disponible' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El activo no está disponible para asignación.';
    ELSE
        SELECT id_estado_activo INTO v_id_estado_asignado
        FROM estado_activo WHERE nombre_estado = 'Asignado';

        INSERT INTO asignacion_activo (id_activo, id_usuario_responsable, id_area, observaciones)
        VALUES (p_id_activo, p_id_usuario_responsable, v_id_area, p_observaciones);

        UPDATE activo
        SET id_estado_activo = v_id_estado_asignado
        WHERE id_activo = p_id_activo;
    END IF;
END //

CREATE PROCEDURE sp_registrar_ticket(
    IN p_id_activo INT,
    IN p_id_usuario_solicitante INT,
    IN p_descripcion_falla TEXT,
    IN p_prioridad VARCHAR(20)
)
BEGIN
    DECLARE v_id_ans INT;
    DECLARE v_id_estado_abierto INT;
    DECLARE v_id_ticket INT;

    SELECT id_ans INTO v_id_ans
    FROM ans
    WHERE prioridad = p_prioridad AND estado_ans = 'Activo'
    ORDER BY tiempo_solucion_horas ASC
    LIMIT 1;

    SELECT id_estado_ticket INTO v_id_estado_abierto
    FROM estado_ticket
    WHERE nombre_estado = 'Abierto';

    INSERT INTO ticket (id_activo, id_usuario_solicitante, id_estado_ticket, id_ans, descripcion_falla, prioridad)
    VALUES (p_id_activo, p_id_usuario_solicitante, v_id_estado_abierto, v_id_ans, p_descripcion_falla, p_prioridad);

    SET v_id_ticket = LAST_INSERT_ID();

    INSERT INTO mantenimiento (id_activo, id_tipo_mantenimiento, id_ticket, id_usuario_solicitante, descripcion, prioridad, estado_mantenimiento)
    VALUES (
        p_id_activo,
        (SELECT id_tipo_mantenimiento FROM tipo_mantenimiento WHERE nombre_tipo='Correctivo'),
        v_id_ticket,
        p_id_usuario_solicitante,
        p_descripcion_falla,
        p_prioridad,
        'Programado'
    );

    SELECT v_id_ticket AS id_ticket_creado;
END //

CREATE PROCEDURE sp_asignar_tecnico_ticket(
    IN p_id_ticket INT,
    IN p_id_usuario_tecnico INT,
    IN p_observaciones TEXT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM usuario u
        INNER JOIN rol r ON u.id_rol = r.id_rol
        WHERE u.id_usuario = p_id_usuario_tecnico
          AND r.nombre_rol = 'Técnico de soporte'
          AND u.estado_usuario = 'Activo'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario seleccionado no es un técnico de soporte activo.';
    ELSE
        INSERT INTO asignacion_tecnico (id_ticket, id_usuario_tecnico, observaciones)
        VALUES (p_id_ticket, p_id_usuario_tecnico, p_observaciones);

        UPDATE ticket
        SET id_estado_ticket = (SELECT id_estado_ticket FROM estado_ticket WHERE nombre_estado='Asignado')
        WHERE id_ticket = p_id_ticket;
    END IF;
END //

CREATE PROCEDURE sp_carga_actual_tecnicos()
BEGIN
    SELECT 
        u.id_usuario AS id_tecnico,
        u.nombre_completo AS tecnico,
        SUM(CASE WHEN et.nombre_estado IN ('Abierto', 'Asignado', 'En proceso') THEN 1 ELSE 0 END) AS tickets_asignados_actualmente
    FROM usuario u
    INNER JOIN rol r ON u.id_rol = r.id_rol
    LEFT JOIN asignacion_tecnico atec ON u.id_usuario = atec.id_usuario_tecnico
    LEFT JOIN ticket t ON atec.id_ticket = t.id_ticket
    LEFT JOIN estado_ticket et ON t.id_estado_ticket = et.id_estado_ticket
    WHERE r.nombre_rol = 'Técnico de soporte'
      AND u.estado_usuario = 'Activo'
    GROUP BY u.id_usuario, u.nombre_completo
    ORDER BY tickets_asignados_actualmente ASC, tecnico ASC;
END //


CREATE PROCEDURE sp_tickets_fuera_ans()
BEGIN
    SELECT 
        t.id_ticket,
        a.codigo_interno AS activo,
        us.nombre_completo AS solicitante,
        et.nombre_estado AS estado_ticket,
        t.prioridad,
        t.fecha_reporte,
        ans.tiempo_solucion_horas,
        TIMESTAMPDIFF(HOUR, t.fecha_reporte, NOW()) AS horas_transcurridas,
        TIMESTAMPDIFF(HOUR, t.fecha_reporte, NOW()) - ans.tiempo_solucion_horas AS horas_fuera_ans
    FROM ticket t
    INNER JOIN activo a ON t.id_activo = a.id_activo
    INNER JOIN usuario us ON t.id_usuario_solicitante = us.id_usuario
    INNER JOIN estado_ticket et ON t.id_estado_ticket = et.id_estado_ticket
    INNER JOIN ans ON t.id_ans = ans.id_ans
    WHERE et.nombre_estado IN ('Abierto', 'Asignado', 'En proceso')
      AND TIMESTAMPDIFF(HOUR, t.fecha_reporte, NOW()) > ans.tiempo_solucion_horas
    ORDER BY horas_fuera_ans DESC;
END //

CREATE PROCEDURE sp_reporte_activos_por_area()
BEGIN
    SELECT 
        ar.nombre_area,
        ea.nombre_estado,
        COUNT(ac.id_activo) AS total_activos
    FROM activo ac
    LEFT JOIN area ar ON ac.id_area = ar.id_area
    INNER JOIN estado_activo ea ON ac.id_estado_activo = ea.id_estado_activo
    GROUP BY ar.nombre_area, ea.nombre_estado
    ORDER BY ar.nombre_area, ea.nombre_estado;
END //

CREATE PROCEDURE sp_licencias_por_vencer(IN p_dias INT)
BEGIN
    SELECT 
        id_licencia,
        nombre_software,
        tipo_licencia,
        codigo_licencia,
        fecha_vencimiento,
        DATEDIFF(fecha_vencimiento, CURDATE()) AS dias_restantes,
        estado_licencia
    FROM licencia_software
    WHERE fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_dias DAY)
      AND estado_licencia = 'Activa'
    ORDER BY fecha_vencimiento ASC;
END //

DELIMITER ;

CALL sp_registrar_ticket(
    (SELECT id_activo FROM activo WHERE codigo_interno = 'ACT-001'),
    (SELECT id_usuario FROM usuario WHERE documento = '9003'),
    'El equipo no enciende',
    'Alta'
);

CALL sp_registrar_ticket(
    (SELECT id_activo FROM activo WHERE codigo_interno = 'ACT-002'),
    (SELECT id_usuario FROM usuario WHERE documento = '9003'),
    'El equipo está lento',
    'Media'
);

CALL sp_asignar_tecnico_ticket(
    1,
    (SELECT id_usuario FROM usuario WHERE documento = '9001'),
    'Asignado a Carlos'
);

CALL sp_asignar_tecnico_ticket(
    2,
    (SELECT id_usuario FROM usuario WHERE documento = '9002'),
    'Asignado a Ana'
);


UPDATE usuario
SET contrasena_hash = 'admin123'
WHERE nombre_usuario = 'admin';

UPDATE usuario
SET contrasena_hash = 'coord123'
WHERE nombre_usuario = 'lcoordinadora';

UPDATE usuario
SET contrasena_hash = 'ctec123'
WHERE nombre_usuario = 'ctecnico';

UPDATE usuario
SET contrasena_hash = 'atec123'
WHERE nombre_usuario = 'atecnica';

UPDATE usuario
SET contrasena_hash = 'juser123'
WHERE nombre_usuario = 'jusuario';
