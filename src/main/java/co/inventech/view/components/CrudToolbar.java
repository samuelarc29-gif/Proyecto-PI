package co.inventech.view.components;

import co.inventech.util.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CrudToolbar extends JPanel {
    public final JTextField search = new JTextField();
    public final JButton btnRefrescar = new JButton("Refrescar");
    public final JButton btnNuevo = new JButton("+ Nuevo");
    public final JButton btnEditar = new JButton("Editar");
    public final JButton btnEliminar = new JButton("Eliminar");

    public CrudToolbar(String placeholder) {
        setOpaque(false);
        setLayout(new BorderLayout(12, 0));
        setBorder(new EmptyBorder(0, 0, 14, 0));

        search.putClientProperty("JTextField.placeholderText", placeholder);
        search.setPreferredSize(new Dimension(260, 36));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(search);
        left.add(Box.createHorizontalStrut(8));
        left.add(btnRefrescar);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        btnNuevo.setBackground(UITheme.ACCENT_TEAL); btnNuevo.setForeground(Color.WHITE);
        btnEditar.setBackground(UITheme.ACCENT_BLUE); btnEditar.setForeground(Color.WHITE);
        btnEliminar.setBackground(UITheme.ACCENT_RED); btnEliminar.setForeground(Color.WHITE);
        for (JButton b : new JButton[]{btnNuevo, btnEditar, btnEliminar}) {
            b.setFocusPainted(false); b.setFont(UITheme.font(12, Font.BOLD));
            b.setPreferredSize(new Dimension(110, 36));
        }
        right.add(btnNuevo); right.add(btnEditar); right.add(btnEliminar);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }
}
