import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.ui.*;
import com.hallsymphony.util.DataInitializer;
import com.hallsymphony.util.StyleConfig;

public class TestLogin {
    public static void main(String[] args) {
        try {
            StyleConfig.applyGlobalTheme();
            DataInitializer.initialize();
            
            AuthService.login("dipen", "dipen123").ifPresentOrElse(
                u -> {
                    System.out.println("Login success");
                    try {
                        Customer c = (Customer) u;
                        MainFrame mf = new MainFrame();
                        System.out.println("MainFrame created");
                        CustomerDashboard dash = new CustomerDashboard(mf, c);
                        System.out.println("Dashboard created success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                () -> System.out.println("Login failed")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
