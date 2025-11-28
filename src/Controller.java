import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Controller {
    private User user;
    private Channel channel;
    private Channel brandChannel;
    private static VideoList publicList;
    private static Controller instance;

    private Controller() {
        publicList = new VideoList();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public boolean login(String email, String password) throws SQLException {
        user = User.login(email, password);
        return user != null;
    }
}
