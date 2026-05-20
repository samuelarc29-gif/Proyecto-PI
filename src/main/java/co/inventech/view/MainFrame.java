package co.inventech.view;

import co.inventech.util.Sesion;
import co.inventech.util.UITheme;
import co.inventech.view.components.Sidebar;
import co.inventech.view.panels.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final JPanel content = new JPanel(new CardLayout());
    private final Sidebar sidebar;

    public MainFrame() {
        setTitle("Inventech · Sistema de Gestión de Activos Tecnológicos");
        setSize(1380, 820);
        setMinimumSize(new Dimension(1180, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(UITheme.BG_PRIMARY);
        setLayout(new BorderLayout());

        sidebar = new Sidebar(this::show);
        add(sidebar, BorderLayout.WEST);

        content.setBackground(UITheme.BG_PRIMARY);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        content.add(new DashboardPanel(), "dashboard");
        content.add(new ActivosPanel(), "activos");
        content.add(new TicketsPanel(), "tickets");
        content.add(new MantenimientosPanel(), "mantenimientos");
        content.add(new LicenciasPanel(), "licencias");
        content.add(new ProveedoresPanel(), "proveedores");
        content.add(new AnsPanel(), "ans");
        content.add(new UsuariosPanel(), "usuarios");
        content.add(new ReportesPanel(), "reportes");

        add(buildTopBar(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        sidebar.setActivo("dashboard");
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UITheme.BG_SECONDARY);
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER),
                new EmptyBorder(10, 24, 10, 24)));

        JLabel left = new JLabel("Panel principal");
        left.setForeground(UITheme.TEXT_PRIMARY);
        left.setFont(UITheme.font(14, Font.BOLD));

        JButton logout = new JButton("Cerrar sesión");
        logout.setFocusPainted(false);
        logout.setBackground(UITheme.BG_CARD);
        logout.setForeground(UITheme.TEXT_PRIMARY);
        logout.addActionListener(e -> {
            Sesion.cerrar();
            dispose();
            new LoginFrame().setVisible(true);
        });

        JLabel user = new JLabel("  " + (Sesion.usuario()==null?"":Sesion.usuario().getNombreCompleto()));
        user.setForeground(UITheme.TEXT_MUTED);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(user); right.add(logout);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private void show(String key) {
        ((CardLayout) content.getLayout()).show(content, key);
    }
}
