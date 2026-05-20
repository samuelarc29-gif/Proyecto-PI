package co.inventech;
import co.inventech.util.UITheme;
import co.inventech.view.LoginFrame;
import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        UITheme.install();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
