package ua.deti.tqs.easydeliversadmin.selenium;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;

import java.util.List;

@ExtendWith(SeleniumJupiter.class)
public class LoginUnsuccessfullTest {

  @BeforeEach
  public void setUp(FirefoxDriver driver) {
    driver.get("http://localhost:8080/login");
    if ((driver.findElements(By.cssSelector(".simple-text")).size() > 0) && (driver.findElement(By.cssSelector(".simple-text")).getText().equals("EASY DELIVERS"))) {
      driver.findElement(By.id("navbarDropdownProfile")).click();
      driver.findElement(By.xpath("/html/body/div/div[2]/nav/div/div/ul/li[3]/div/a[2]")).click();
    }
  }

  @Test
  public void loginUnsuccessfullTest(FirefoxDriver driver) {
    driver.get("http://localhost:8080/login");
    driver.manage().window().setSize(new Dimension(1900, 1000));
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).sendKeys("WRONG_EMAIL");
    driver.findElement(By.name("password")).click();
    driver.findElement(By.name("password")).sendKeys("WRONG_PASS");
    driver.findElement(By.name("signup_submit")).click();
    {
      List<WebElement> elements = driver.findElements(By.cssSelector(".img > img:nth-child(1)"));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector("h2")).getText(), is("Internal Server Error"));
  }
}

