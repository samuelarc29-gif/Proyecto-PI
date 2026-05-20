package co.inventech.view;

import co.inventech.controller.AuthController;
import co.inventech.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtClave = new JPasswordField();
    private final JLabel lblError = new JLabel(" ");
    private final AuthController auth = new AuthController();

    public LoginFrame() {
        setTitle("Inventech · Iniciar sesión");
        setSize(960, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG_PRIMARY);
        setLayout(new BorderLayout());

        add(buildHero(), BorderLayout.WEST);
        add(buildForm(), BorderLayout.CENTER);
    }

    private JPanel buildHero() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0B1220), 0, getHeight(), new Color(0x102A43));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 18));
                for (int i = 0; i < 6; i++) {
                    g2.fillOval(-80 + i * 70, getHeight() - 220 + i * 30, 220, 220);
                }
                g2.dispose();
            }
        };

        hero.setPreferredSize(new Dimension(420, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(60, 35, 20, 50));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(285, 285, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel logoWrapper = new JPanel();
        logoWrapper.setOpaque(false);
        logoWrapper.setLayout(new BoxLayout(logoWrapper, BoxLayout.X_AXIS));
        logoWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoWrapper.setMaximumSize(new Dimension(335, 285));
        logoWrapper.add(Box.createHorizontalGlue());
        logoWrapper.add(logoLabel);
        logoWrapper.add(Box.createHorizontalGlue());

        JLabel brand = new JLabel("INVENTECH");
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setHorizontalAlignment(SwingConstants.LEFT);
        brand.setForeground(Color.WHITE);
        brand.setFont(UITheme.font(28, Font.BOLD));

        JLabel sub = new JLabel("<html><body style='width:280px;color:#9CA3AF;text-align:left;'>"
                + "Gestión integral de activos tecnológicos, soporte y mantenimientos.</body></html>");
        sub.setFont(UITheme.font(13));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel feat = new JLabel("<html><body style='width:280px;color:#CBD5E1;line-height:1.7;text-align:left;'>"
                + "&#9679; Inventario y trazabilidad de activos<br>"
                + "&#9679; Mesa de ayuda y tickets ANS<br>"
                + "&#9679; Mantenimiento preventivo y correctivo<br>"
                + "&#9679; Control de licencias de software<br>"
                + "&#9679; Reportes administrativos</body></html>");
        feat.setFont(UITheme.font(12));
        feat.setAlignmentX(Component.LEFT_ALIGNMENT);
        feat.setHorizontalAlignment(SwingConstants.LEFT);

        content.add(brand);
        content.add(Box.createVerticalStrut(12));
        content.add(sub);
        content.add(Box.createVerticalStrut(36));
        content.add(feat);
        content.add(Box.createVerticalStrut(18));
        content.add(logoWrapper);
        content.add(Box.createVerticalGlue());

        JLabel foot = new JLabel("© 2025 Inventech · Proyecto Integrador");
        foot.setForeground(new Color(0x6B7280));
        foot.setFont(UITheme.font(11));
        foot.setAlignmentX(Component.LEFT_ALIGNMENT);
        foot.setHorizontalAlignment(SwingConstants.LEFT);

        content.add(foot);

        hero.add(content, BorderLayout.CENTER);
        return hero;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(UITheme.BG_PRIMARY);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_SECONDARY);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(34, 38, 34, 38)));
        card.setPreferredSize(new Dimension(380, 420));

        JLabel title = new JLabel("Bienvenido de vuelta");
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setFont(UITheme.font(22, Font.BOLD));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hint = new JLabel("Ingresa tus credenciales para acceder.");
        hint.setForeground(UITheme.TEXT_MUTED);
        hint.setFont(UITheme.font(12));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(hint);
        card.add(Box.createVerticalStrut(28));
        card.add(label("Usuario"));
        card.add(Box.createVerticalStrut(6));

        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsuario.putClientProperty("JTextField.placeholderText", "admin");
        card.add(txtUsuario);

        card.add(Box.createVerticalStrut(16));
        card.add(label("Contraseña"));
        card.add(Box.createVerticalStrut(6));

        txtClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtClave.putClientProperty("JPasswordField.placeholderText", "••••••••");
        card.add(txtClave);

        card.add(Box.createVerticalStrut(8));

        lblError.setForeground(UITheme.ACCENT_RED);
        lblError.setFont(UITheme.font(11));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblError);

        card.add(Box.createVerticalStrut(8));

        JButton btn = new JButton("Iniciar sesión");
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setBackground(UITheme.ACCENT_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(UITheme.font(13, Font.BOLD));
        btn.setFocusPainted(false);
        btn.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(btn);

        card.add(btn);

        wrap.add(card);
        return wrap;
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(UITheme.TEXT_MUTED);
        l.setFont(UITheme.font(11, Font.BOLD));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void doLogin() {
        String u = txtUsuario.getText().trim();
        String c = new String(txtClave.getPassword());

        if (u.isEmpty() || c.isEmpty()) {
            lblError.setText("Completa todos los campos.");
            return;
        }

        try {
            if (auth.login(u, c)) {
                dispose();
                SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
            } else {
                lblError.setText("Credenciales inválidas o usuario inactivo.");
            }
        } catch (SQLException ex) {
            lblError.setText("Error de conexión: " + ex.getMessage());
        }
    }
}