package tqs.proudpapers;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author wy
 * @date 2021/6/19 22:50
 */
@ExtendWith(SeleniumJupiter.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationTest {
    private static WebDriver driver;

    @LocalServerPort
    private int port;

    private static ClientDTO clientDTO = new ClientDTO();

    @BeforeAll
    static void setUp() {
        driver = new ChromeDriver();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Order(1)
    @Test
    public void toToSingUpPage() {
        driver.get("http://localhost:" + port + "/");
        driver.manage().window().setSize(new Dimension(1058, 900));
        assertThat(driver.getTitle(), is("Index"));
        driver.findElement(By.linkText("Login")).click();
        assertThat(driver.getTitle(), is("Login"));
        assertThat(driver.findElement(By.linkText("Sign up")).getText(), is("Sign up"));
        driver.findElement(By.linkText("Sign up")).click();
    }

    @Order(2)
    @Test
    public void fillSignUpForm(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(r.nextInt(9));
        }
        sb.append("@ua.pt");
        clientDTO.setEmail(sb.toString());
        clientDTO.setPassword("abcd");
        clientDTO.setName("wei");
        clientDTO.setZip("2222-222");
        clientDTO.setCity("aveiro");
        clientDTO.setTelephone("12312313");

        PaymentMethod p = new PaymentMethod();
        p.setCardExpirationMonth("12");
        p.setCardNumber("123123123");
        p.setCvc("123");

        clientDTO.setPaymentMethod(p);

        driver.findElement(By.id("email")).sendKeys(clientDTO.getEmail());
        driver.findElement(By.id("password")).sendKeys(clientDTO.getPassword());
        driver.findElement(By.cssSelector("body")).click();
        driver.findElement(By.id("name")).click();
        driver.findElement(By.id("name")).sendKeys(clientDTO.getName());
        {
            WebElement element = driver.findElement(By.id("email"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).clickAndHold().perform();
        }
        {
            WebElement element = driver.findElement(By.id("email"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.id("email"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).release().perform();
        }
        driver.findElement(By.id("email")).click();
        driver.findElement(By.cssSelector(".login-container")).click();
        driver.findElement(By.id("zip")).click();
        driver.findElement(By.id("zip")).sendKeys(clientDTO.getZip());
        driver.findElement(By.id("city")).click();
        driver.findElement(By.id("city")).sendKeys(clientDTO.getCity());
        driver.findElement(By.id("telephone")).click();
        driver.findElement(By.id("telephone")).sendKeys(clientDTO.getTelephone());
        driver.findElement(By.id("cardNumber")).click();
        driver.findElement(By.id("cardNumber")).sendKeys(clientDTO.getPaymentMethod().getCardNumber());
        driver.findElement(By.id("cardExpirationMonth")).click();
        driver.findElement(By.id("cardExpirationMonth")).sendKeys(clientDTO.getPaymentMethod().getCardExpirationMonth());
        driver.findElement(By.id("cvc")).click();
        driver.findElement(By.id("cvc")).sendKeys(clientDTO.getPaymentMethod().getCvc());
        driver.findElement(By.id("submit-btn")).click();
    }

    @Order(3)
    @Test
    public void login() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        assertThat(driver.getTitle(), is("Login"));
        driver.findElement(By.id("email")).sendKeys(clientDTO.getEmail());
        driver.findElement(By.id("Password")).sendKeys(clientDTO.getPassword());
        driver.findElement(By.cssSelector(".btn")).click();
    }

    @Order(4)
    @Test
    public void searchBookAndAddItToCart() throws InterruptedException {
        assertThat(driver.getTitle(), is("Index"));
        assertThat(driver.findElement(By.linkText(clientDTO.getName())).getText(), is(clientDTO.getName()));
        driver.findElement(By.cssSelector(".search-input")).click();
        driver.findElement(By.cssSelector(".search-input")).sendKeys("at");
        driver.findElement(By.cssSelector(".search-input")).sendKeys(Keys.ENTER);
        assertThat(driver.getTitle(), is("Search"));
        driver.findElement(By.cssSelector(".row:nth-child(1) > .col-md-10 .product-name")).click();
        assertThat(driver.getTitle(), is("Proud Papers"));
        assertThat(driver.findElement(By.id("product-name")).getText(), is("Atmamun"));
        assertThat(driver.findElement(By.id("product-price")).getText(), is("15.99"));
        driver.findElement(By.cssSelector(".add-to-cart-btn")).click();
        TimeUnit.SECONDS.sleep(1);
        assertThat(driver.findElement(By.id("product-cart-quantity")).getText(), is("1"));
    }

    @Order(5)
    @Test
    public void accountPage() throws InterruptedException {
        driver.findElement(By.linkText(clientDTO.getName())).click();
        assertThat(driver.getTitle(), is("Proud Papers"));
        assertThat(driver.findElement(By.linkText("Address")).getText(), is("Address"));
        assertThat(driver.findElement(By.linkText("Contact")).getText(), is("Contact"));
        assertThat(driver.findElement(By.linkText("Payment Method")).getText(), is("Payment Method"));
        assertThat(driver.findElement(By.linkText("Cart")).getText(), is("Cart"));
        assertThat(driver.findElement(By.linkText("Deliveries")).getText(), is("Deliveries"));
        driver.findElement(By.linkText("Address")).click();
        driver.findElement(By.linkText("Contact")).click();
        driver.findElement(By.linkText("Payment Method")).click();
        driver.findElement(By.linkText("Cart")).click();
        assertThat(driver.findElement(By.cssSelector(".mb-0")).getText(), is("Atmamun"));
        driver.findElement(By.linkText("Deliveries")).click();
        driver.findElement(By.linkText("Cart")).click();
        driver.findElement(By.cssSelector(".buy-btn")).click();
        TimeUnit.MILLISECONDS.sleep(500);
        assertThat(driver.findElement(By.id("exampleModalLabel")).getText(), is("Shipping details"));

    }
}
