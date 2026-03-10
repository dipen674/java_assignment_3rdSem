import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.DataInitializer;
public class TestLogin {
    public static void main(String[] args) {
        DataInitializer.initialize();
        AuthService.login("dipen", "dipen123").ifPresentOrElse(
            u -> System.out.println("Login success: " + u.getRole() + " " + u.getClass().getName()),
            () -> System.out.println("Login failed")
        );
    }
}
