package com.inventech.view;

import java.util.ArrayList;
import java.util.List;

/** Determina las opciones de menú visibles según el rol del usuario. */
public class MenuPorRol {

    public static List<String[]> opcionesPara(String rol) {
        List<String[]> ops = new ArrayList<>();
        ops.add(new String[]{"Dashboard", "DASHBOARD"});

        if (rol == null) return ops;
        switch (rol) {
            case "Administrador del sistema" -> {
                ops.add(new String[]{"Usuarios", "USUARIOS"});
                ops.add(new String[]{"Activos", "ACTIVOS"});
                ops.add(new String[]{"Marcas", "MARCAS"});
                ops.add(new String[]{"Proveedores", "PROVEEDORES"});
                ops.add(new String[]{"Asignaciones", "ASIGNACIONES"});
                ops.add(new String[]{"Tickets", "TICKETS"});
                ops.add(new String[]{"Mantenimientos", "MANTENIMIENTOS"});
                ops.add(new String[]{"Licencias", "LICENCIAS"});
                ops.add(new String[]{"ANS", "ANS"});
                ops.add(new String[]{"Reportes", "REPORTES"});
            }
            case "Responsable de área" -> {
                ops.add(new String[]{"Activos del área", "ACTIVOS"});
                ops.add(new String[]{"Asignaciones", "ASIGNACIONES"});
                ops.add(new String[]{"Reportes", "REPORTES"});
            }
            case "Usuario general" -> {
                ops.add(new String[]{"Mis activos", "MIS_ACTIVOS"});
                ops.add(new String[]{"Crear ticket", "TICKETS"});
            }
            case "Proveedor" -> {
                ops.add(new String[]{"Activos suministrados", "ACTIVOS"});
                ops.add(new String[]{"Mi información", "PROVEEDORES"});
            }
            case "Coordinador de soporte técnico" -> {
                ops.add(new String[]{"Tickets", "TICKETS"});
                ops.add(new String[]{"Asignar técnicos", "ASIGNACION_TECNICOS"});
                ops.add(new String[]{"ANS", "ANS"});
                ops.add(new String[]{"Reportes", "REPORTES"});
            }
            case "Técnico de soporte" -> {
                ops.add(new String[]{"Mis tickets", "TICKETS"});
                ops.add(new String[]{"Mantenimientos", "MANTENIMIENTOS"});
            }
        }
        return ops;
    }
}
