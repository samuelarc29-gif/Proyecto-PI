package com.inventech;

import com.formdev.flatlaf.FlatDarkLaf;
import com.inventech.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Acentos verde/azul Inventech
            UIManager.put("Component.focusColor", new java.awt.Color(0, 200, 130));
            UIManager.put("Component.focusedBorderColor", new java.awt.Color(0, 200, 130));
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLaf: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
