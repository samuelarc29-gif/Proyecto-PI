package com.inventech.view;

import com.inventech.dao.DashboardDAO;
import com.inventech.util.Sesion;
import com.inventech.util.Tema;
import com.inventech.view.crud.ActivoView;
import com.inventech.view.crud.AnsView;
import com.inventech.view.crud.AsignacionActivoView;
import com.inventech.view.crud.AsignacionTecnicoView;
import com.inventech.view.crud.LicenciaSoftwareView;
import com.inventech.view.crud.MantenimientoView;
import com.inventech.view.crud.MarcaView;
import com.inventech.view.crud.ProveedorView;
import com.inventech.view.crud.ReporteView;
import com.inventech.view.crud.TicketView;
import com.inventech.view.crud.UsuarioView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardView extends JFrame {

    private final JPanel contenido = new JPanel(new BorderLayout());

    public DashboardView() {
        setTitle("Inventech - Panel principal");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Tema.FONDO);
        setLayout(new BorderLayout());

        add(construirSidebar(), BorderLayout.WEST);
        add(construirHeader(), BorderLayout.NORTH);
        contenido.setBackground(Tema.FONDO);
        add(contenido, BorderLayout.CENTER);
        mostrarResumen();
    }

    private JPanel construirHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(new Color(10, 16, 30));
        h.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel logo = new JLabel("INVENTECH");
        logo.setForeground(Tema.VERDE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel info = new JLabel(
                String.format("Sesión: %s  |  Rol: %s",
                        Sesion.getUsuario().getNombreCompleto(), Sesion.getRol()));
        info.setForeground(Tema.TEXTO);
        h.add(logo, BorderLayout.WEST);
        h.add(info, BorderLayout.EAST);
        return h;
    }

    private JPanel construirSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Tema.PANEL);
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(new EmptyBorder(20, 14, 20, 14));

        String rol = Sesion.getRol();
        List<String[]> menu = MenuPorRol.opcionesPara(rol);

        for (String[] opt : menu) {
            JButton btn = botonMenu(opt[0]);
            String accion = opt[1];
            btn.addActionListener(e -> abrir(accion));
            side.add(btn);
            side.add(Box.createVerticalStrut(6));
        }

        side.add(Box.createVerticalGlue());
        JButton logout = botonMenu("Cerrar sesión");
        logout.setBackground(Tema.PELIGRO);
        logout.addActionListener(e -> {
            Sesion.cerrar();
            dispose();
            new LoginView().setVisible(true);
        });
        side.add(logout);
        return side;
    }

    private JButton botonMenu(String texto) {
        JButton b = new JButton(texto);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setBackground(Tema.AZUL);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        return b;
    }

    private void abrir(String accion) {
        contenido.removeAll();
        switch (accion) {
            case "DASHBOARD" -> mostrarResumen();
            case "USUARIOS"  -> contenido.add(new UsuarioView(), BorderLayout.CENTER);
            case "ACTIVOS", "MIS_ACTIVOS" -> contenido.add(new ActivoView(), BorderLayout.CENTER);
            case "MARCAS"    -> contenido.add(new MarcaView(), BorderLayout.CENTER);
            case "PROVEEDORES" -> contenido.add(new ProveedorView(), BorderLayout.CENTER);
            case "ASIGNACIONES" -> contenido.add(new AsignacionActivoView(), BorderLayout.CENTER);
            case "TICKETS" -> contenido.add(new TicketView(), BorderLayout.CENTER);
            case "MANTENIMIENTOS" -> contenido.add(new MantenimientoView(), BorderLayout.CENTER);
            case "LICENCIAS" -> contenido.add(new LicenciaSoftwareView(), BorderLayout.CENTER);
            case "ANS" -> contenido.add(new AnsView(), BorderLayout.CENTER);
            case "ASIGNACION_TECNICOS" -> contenido.add(new AsignacionTecnicoView(), BorderLayout.CENTER);
            case "REPORTES" -> contenido.add(new ReporteView(), BorderLayout.CENTER);
            default -> {
                JLabel l = new JLabel("Módulo '" + accion + "' pendiente de implementación.", SwingConstants.CENTER);
                l.setForeground(Tema.TEXTO_MUTED);
                contenido.add(l, BorderLayout.CENTER);
            }
        }
        contenido.revalidate();
        contenido.repaint();
    }

    private void mostrarResumen() {
        contenido.removeAll();
        JPanel grid = new JPanel(new GridLayout(2, 4, 16, 16));
        grid.setBackground(Tema.FONDO);
        grid.setBorder(new EmptyBorder(24, 24, 24, 24));
        try {
            Map<String, Integer> r = new DashboardDAO().obtenerResumen();
            grid.add(tarjeta("Total activos", r.getOrDefault("totalActivos", 0), Tema.AZUL));
            grid.add(tarjeta("Disponibles", r.getOrDefault("disponibles", 0), Tema.VERDE));
            grid.add(tarjeta("Asignados", r.getOrDefault("asignados", 0), Tema.AZUL));
            grid.add(tarjeta("En mantenimiento", r.getOrDefault("mantenimiento", 0), Tema.PELIGRO));
            grid.add(tarjeta("Tickets abiertos", r.getOrDefault("ticketsAbiertos", 0), Tema.AZUL));
            grid.add(tarjeta("Tickets en proceso", r.getOrDefault("ticketsProceso", 0), Tema.VERDE));
            grid.add(tarjeta("Licencias por vencer", r.getOrDefault("licenciasPorVencer", 0), Tema.PELIGRO));
        } catch (Exception ex) {
            JLabel l = new JLabel("Error al cargar dashboard: " + ex.getMessage());
            l.setForeground(Tema.PELIGRO);
            grid.add(l);
        }
        contenido.add(grid, BorderLayout.NORTH);
        contenido.revalidate();
        contenido.repaint();
    }

    private JPanel tarjeta(String titulo, int valor, Color acento) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Tema.PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, acento),
                new EmptyBorder(16, 18, 16, 18)));
        JLabel t = new JLabel(titulo);
        t.setForeground(Tema.TEXTO_MUTED);
        JLabel v = new JLabel(String.valueOf(valor));
        v.setForeground(Tema.TEXTO);
        v.setFont(new Font("Segoe UI", Font.BOLD, 32));
        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }
}
