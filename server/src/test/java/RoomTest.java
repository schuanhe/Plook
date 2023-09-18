import com.schuanhe.Plook.SpringbootApplication;
import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.entity.User;
import com.schuanhe.Plook.service.RoomService;
import com.schuanhe.Plook.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringbootApplication.class)
@ContextConfiguration
public class RoomTest {


}
