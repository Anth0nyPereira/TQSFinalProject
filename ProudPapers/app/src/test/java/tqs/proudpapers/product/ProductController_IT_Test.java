package tqs.proudpapers.product;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tqs.proudpapers.ProudPapersApplication;

/**
 * @author wy
 * @date 2021/6/16 17:49
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProudPapersApplication.class)
@AutoConfigureMockMvc
public class ProductController_IT_Test {
}
