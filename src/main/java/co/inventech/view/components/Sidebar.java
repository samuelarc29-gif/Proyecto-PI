package co.inventech.view.components;

import co.inventech.util.UITheme;
import co.inventech.util.Sesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    public record MenuItem(String key, String label, String icon, String permiso) {}

    private final LinkedHashMap<String, JButton> botones = new LinkedHashMap<>();
    private String activo;

    public Sidebar(Consumer<String> onSelect) {
        setBackground(UITheme.BG_SIDEBAR);
        setPreferredSize(new Dimension(240, 0));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(18, 14, 12, 14));
        top.add(new LogoPanel("Asset Management"));
        top.add(Box.createVerticalStrut(8));

        for (MenuItem mi : items()) {
            if (mi.permiso != null && !mi.permiso.isEmpty() && !Sesion.tienePermiso(mi.permiso)
                    && !Sesion.tienePermiso("GESTIONAR_USUARIOS")) continue; // admin ve todo
            JButton b = makeButton(mi);
            botones.put(mi.key, b);
            b.addActionListener(e -> { setActivo(mi.key); onSelect.accept(mi.key); });
            top.add(b);
            top.add(Box.createVerticalStrut(4));
        }

        add(new JScrollPane(top, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER);

        // Pie con usuario
        JPanel foot = new JPanel(new BorderLayout());
        foot.setOpaque(false);
        foot.setBorder(new EmptyBorder(12, 16, 16, 16));
        var u = Sesion.usuario();
        JLabel name = new JLabel(u != null ? u.getNombreCompleto() : "—");
        name.setForeground(UITheme.TEXT_PRIMARY);
        name.setFont(UITheme.font(12, Font.BOLD));
        JLabel rol = new JLabel(u != null ? u.getRolNombre() : "");
        rol.setForeground(UITheme.TEXT_MUTED);
        rol.setFont(UITheme.font(10));
        JPanel info = new JPanel(); info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(name); info.add(rol);
        foot.add(info, BorderLayout.CENTER);
        add(foot, BorderLayout.SOUTH);
    }

    private java.util.List<MenuItem> items() {
        return java.util.List.of(
            new MenuItem("dashboard", "Dashboard", "▣", ""),
            new MenuItem("activos", "Activos", "▦", "GESTIONAR_ACTIVOS"),
            new MenuItem("tickets", "Mesa de ayuda", "✉", "GESTIONAR_TICKETS"),
            new MenuItem("mantenimientos", "Mantenimientos", "✦", "GESTIONAR_MANTENIMIENTOS"),
            new MenuItem("licencias", "Licencias", "◌", "GESTIONAR_LICENCIAS"),
            new MenuItem("proveedores", "Proveedores", "◈", "GESTIONAR_PROVEEDORES"),
            new MenuItem("ans", "ANS", "◐", "GESTIONAR_ANS"),
            new MenuItem("usuarios", "Usuarios y roles", "◉", "GESTIONAR_USUARIOS"),
            new MenuItem("reportes", "Reportes", "▤", "GENERAR_REPORTES")
        );
    }

    private JButton makeButton(MenuItem mi) {
        JButton b = new JButton("   " + mi.icon + "    " + mi.label);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(UITheme.font(13));
        b.setForeground(UITheme.TEXT_MUTED);
        b.setBackground(UITheme.BG_SIDEBAR);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.putClientProperty("JButton.buttonType", "borderless");
        return b;
    }

    public void setActivo(String key) {
        this.activo = key;
        for (Map.Entry<String, JButton> e : botones.entrySet()) {
            boolean sel = e.getKey().equals(key);
            e.getValue().setBackground(sel ? UITheme.ACCENT_BLUE : UITheme.BG_SIDEBAR);
            e.getValue().setForeground(sel ? Color.WHITE : UITheme.TEXT_MUTED);
            e.getValue().setFont(UITheme.font(13, sel ? Font.BOLD : Font.PLAIN));
        }
    }
}
