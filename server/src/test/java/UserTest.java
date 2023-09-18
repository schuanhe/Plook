import com.schuanhe.Plook.SpringbootApplication;
import com.schuanhe.Plook.entity.User;
import com.schuanhe.Plook.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringbootApplication.class)
@ContextConfiguration
public class UserTest {

    @Autowired
    UserService userService;

    /**
     * 登录测试
     * 如果账号密码正确则登录成功且返回正确的数据，否则返回null
     */
    @Test
    void testLogin(){

    }

    @Test
    void testRegister(){

    }



    @Test
    void testAllUser(){

    }
}
