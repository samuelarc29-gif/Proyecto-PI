package co.inventech.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import java.awt.*;

/** Configuración global del look & feel y colores corporativos Inventech. */
public final class UITheme {
    // Paleta corporativa: azul oscuro + verde tecnología
    public static final Color BG_PRIMARY   = new Color(0x0F1419);
    public static final Color BG_SECONDARY = new Color(0x161B22);
    public static final Color BG_SIDEBAR   = new Color(0x0B1220);
    public static final Color BG_CARD      = new Color(0x1C2128);
    public static final Color ACCENT_BLUE  = new Color(0x2563EB);
    public static final Color ACCENT_TEAL  = new Color(0x10B981);
    public static final Color ACCENT_AMBER = new Color(0xF59E0B);
    public static final Color ACCENT_RED   = new Color(0xEF4444);
    public static final Color TEXT_PRIMARY = new Color(0xE6EDF3);
    public static final Color TEXT_MUTED   = new Color(0x8B949E);
    public static final Color BORDER       = new Color(0x30363D);

    public static void install() {
        try {
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 8);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("TitlePane.unifiedBackground", true);
            UIManager.put("Table.rowHeight", 32);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            FlatOneDarkIJTheme.setup();
        } catch (Exception e) {
            try { FlatDarkLaf.setup(); } catch (Exception ignored) {}
        }
    }

    public static Font font(int size, int style) { return new Font("Segoe UI", style, size); }
    public static Font font(int size) { return font(size, Font.PLAIN); }

    private UITheme() {}
}
