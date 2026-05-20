package co.inventech.view.components;
import co.inventech.util.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;

public abstract class FormDialog extends JDialog {
    protected final JPanel form = new JPanel(new GridBagLayout());
    protected final LinkedHashMap<String, JComponent> fields = new LinkedHashMap<>();
    private int row = 0;

    public FormDialog(Window owner, String title) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        form.setBorder(new EmptyBorder(20, 24, 8, 24));
        form.setBackground(UITheme.BG_SECONDARY);
        JScrollPane sp = new JScrollPane(form);
        sp.setBorder(null);
        add(sp, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        buttons.setBackground(UITheme.BG_SECONDARY);
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());
        JButton guardar = new JButton("Guardar");
        guardar.setBackground(UITheme.ACCENT_BLUE); guardar.setForeground(Color.WHITE);
        guardar.setFont(UITheme.font(12, Font.BOLD));
        guardar.addActionListener(e -> { if (onSave()) dispose(); });
        buttons.add(cancelar); buttons.add(guardar);
        add(buttons, BorderLayout.SOUTH);
    }

    protected void addField(String key, String label, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 4, 12); gbc.weightx = 0;
        JLabel l = new JLabel(label);
        l.setForeground(UITheme.TEXT_MUTED); l.setFont(UITheme.font(11, Font.BOLD));
        form.add(l, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        field.setPreferredSize(new Dimension(320, 32));
        form.add(field, gbc);
        fields.put(key, field);
        row++;
    }

    protected String text(String key) {
        JComponent c = fields.get(key);
        if (c instanceof JTextField tf) return tf.getText().trim();
        if (c instanceof JPasswordField pf) return new String(pf.getPassword());
        if (c instanceof JTextArea ta) return ta.getText().trim();
        if (c instanceof JComboBox<?> cb) return cb.getSelectedItem()==null?"":cb.getSelectedItem().toString();
        return "";
    }

    /** @return true para cerrar el diálogo. */
    protected abstract boolean onSave();

    protected void showCentered(int w, int h) {
        setSize(w, h);
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
}
