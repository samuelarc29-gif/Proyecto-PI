package com.inventech.view.crud;

import com.inventech.util.Tema;

import javax.swing.*;
import java.awt.*;

/** Utilidades comunes para los formularios CRUD. */
final class FormUtils {
    private FormUtils() {}

    static JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Tema.TEXTO_MUTED);
        return l;
    }

    static JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(Tema.FUENTE_SUB);
        return b;
    }

    /** Item simple para JComboBox: id + texto visible. */
    static class Item {
        final int id;
        final String texto;
        Item(int id, String texto) { this.id = id; this.texto = texto; }
        @Override public String toString() { return texto; }
    }

    static int idDe(JComboBox<Item> combo) {
        Item it = (Item) combo.getSelectedItem();
        return it == null ? -1 : it.id;
    }

    static void seleccionarPorId(JComboBox<Item> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).id == id) { combo.setSelectedIndex(i); return; }
        }
    }
}
