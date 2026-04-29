package com.inventech.view;

import com.inventech.controller.LoginController;
import com.inventech.model.Usuario;
import com.inventech.util.Sesion;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private final JTextField txtUsuario = new JTextField(22);
    private final JPasswordField txtPassword = new JPasswordField(22);
    private final LoginController controller = new LoginController();

    public LoginView() {
        setTitle("Inventech - Iniciar sesión");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Tema.FONDO);
        setLayout(new GridLayout(1, 2));

        add(panelMarca());
        add(panelFormulario());
    }

    private JPanel panelMarca() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(10, 16, 30));
        JLabel logo = new JLabel("INVENTECH");
        logo.setForeground(Tema.VERDE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42));

        JLabel sub = new JLabel("Gestión de Activos Tecnológicos");
        sub.setForeground(Tema.AZUL);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(logo);
        inner.add(Box.createVerticalStrut(8));
        inner.add(sub);
        p.add(inner);
        return p;
    }

    private JPanel panelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.PANEL);
        p.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 0, 8, 0);

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(Tema.FUENTE_TITULO);
        titulo.setForeground(Tema.TEXTO);

        JLabel lblU = new JLabel("Correo o documento");
        lblU.setForeground(Tema.TEXTO_MUTED);
        JLabel lblP = new JLabel("Contraseña");
        lblP.setForeground(Tema.TEXTO_MUTED);

        JButton btn = new JButton("INGRESAR");
        btn.setBackground(Tema.VERDE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(Tema.FUENTE_SUB);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.addActionListener(e -> autenticar());

        g.gridy = 0; p.add(titulo, g);
        g.gridy++; p.add(Box.createVerticalStrut(20), g);
        g.gridy++; p.add(lblU, g);
        g.gridy++; p.add(txtUsuario, g);
        g.gridy++; p.add(lblP, g);
        g.gridy++; p.add(txtPassword, g);
        g.gridy++; p.add(Box.createVerticalStrut(10), g);
        g.gridy++; p.add(btn, g);

        return p;
    }

    private void autenticar() {
        try {
            Usuario u = controller.autenticar(txtUsuario.getText());
            if (u == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario no encontrado o inactivo.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // NOTA: la tabla 'usuario' del esquema entregado no tiene columna de contraseña.
            // Si agregas la columna password_hash, valida aquí con BCrypt antes de continuar.
            Sesion.setUsuario(u);
            dispose();
            new DashboardView().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
