package ua.deti.tqs.easydeliversadmin.selenium;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;

public class LogoutSuccessfullTest {

  WebDriver driver;

  @BeforeEach
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver");
    driver = new FirefoxDriver();
    driver.get("http://localhost:8080/login");
    if ((driver.findElements(By.cssSelector(".simple-text")).size() > 0) && (driver.findElement(By.cssSelector(".simple-text")).getText().equals("EASY DELIVERS"))) {
      driver.findElement(By.id("navbarDropdownProfile")).click();
      driver.findElement(By.xpath("/html/body/div/div[2]/nav/div/div/ul/li[3]/div/a[2]")).click();
    }
  }

  @Test
  public void logoutSuccessfullTest() {
    driver.get("http://localhost:8080/login");
    driver.manage().window().setSize(new Dimension(1900, 1000));
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).sendKeys("jaguiar2@gmail.com");
    driver.findElement(By.name("password")).click();
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("signup_submit")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("#navbarDropdownProfile > .material-icons"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector("#navbarDropdownProfile > .material-icons")).click();
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.linkText("Log out")).click();
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Login"));
  }
}

