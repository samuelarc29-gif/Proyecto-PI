package co.inventech.view.panels;
import co.inventech.dao.CatalogoDAO; import co.inventech.dao.TicketDAO;
import co.inventech.model.EstadoTicket; import co.inventech.model.Ticket;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.AsignarTecnicoDialog; import co.inventech.view.dialogs.TicketDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class TicketsPanel extends JPanel {
    private final TicketDAO dao = new TicketDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Activo","Solicitante","Estado","Prioridad","Técnico","Reportado");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar ticket...");
    private final JButton btnAsignar = new JButton("Asignar técnico");
    private final JButton btnEstado = new JButton("Cambiar estado");

    public TicketsPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Mesa de ayuda","Gestión de tickets de soporte técnico y ANS."), BorderLayout.NORTH);
        tb.btnEditar.setVisible(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); actions.setOpaque(false);
        actions.add(btnAsignar); actions.add(btnEstado);
        JPanel north = new JPanel(new BorderLayout()); north.setOpaque(false);
        north.add(tb, BorderLayout.NORTH); north.add(actions, BorderLayout.SOUTH);
        JPanel c = new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(north, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER);
        add(c, BorderLayout.CENTER);

        tb.btnRefrescar.addActionListener(e -> cargar()); tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new TicketDialog(SwingUtilities.getWindowAncestor(this)); cargar(); });
        tb.btnEliminar.addActionListener(e -> { Integer id = selId(); if (id==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar ticket?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(id); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        btnAsignar.addActionListener(e -> { Integer id = selId(); if (id!=null){ new AsignarTecnicoDialog(SwingUtilities.getWindowAncestor(this), id); cargar(); } });
        btnEstado.addActionListener(e -> cambiarEstado());
        cargar();
    }
    private void cambiarEstado() {
        Integer id = selId(); if (id==null) return;
        try {
            var estados = new CatalogoDAO().listarEstadosTicket();
            JComboBox<EstadoTicket> cb = new JComboBox<>(estados.toArray(new EstadoTicket[0]));
            JTextArea obs = new JTextArea(3, 30);
            Object[] msg = { "Nuevo estado:", cb, "Observación:", new JScrollPane(obs) };
            if (JOptionPane.showConfirmDialog(this, msg, "Cambiar estado", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
                EstadoTicket et = (EstadoTicket) cb.getSelectedItem();
                dao.actualizarEstado(id, et.getId(), obs.getText().trim());
                cargar();
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (Ticket t : dao.listar(tb.search.getText()))
                model.addRow(new Object[]{t.getIdTicket(), t.getActivoCodigo(), t.getSolicitanteNombre(), t.getEstadoNombre(), t.getPrioridad(), t.getTecnicoAsignado(), t.getFechaReporte()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Integer selId() { int r=table.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null;} return (int)model.getValueAt(r,0); }
}
