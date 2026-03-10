import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.util.DataInitializer;
import com.hallsymphony.ui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * HallSymphony — Entry Point
 *
 * Hall Booking Management System
 *
 * Compile:
 *   find src -name "*.java" > sources.txt
 *   javac -d bin @sources.txt
 *
 * Run:
 *   java -cp bin HallSymphony
 */
public class HallSymphony {
    public static void main(String[] args) {
        StyleConfig.applyGlobalTheme();
        DataInitializer.initialize();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
