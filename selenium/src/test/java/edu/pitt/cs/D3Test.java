package edu.pitt.cs;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import static org.hamcrest.MatcherAssert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @Before
  public void setUp() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--disable-dev-shm-usage");
    driver = new ChromeDriver(options);
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void tEST1LINKS() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    {
      WebElement element = driver.findElement(By.xpath("//a[contains(text(), \"Reset\")]"));
      String attribute = element.getAttribute("href");
      vars.put("href", attribute);
    }
    assertEquals(vars.get("href").toString(), "http://localhost:8080/reset");
  }

  @Test
  public void tEST2RESET() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    js.executeScript("document.cookie = \"1=true\";document.cookie = \"2=true\";document.cookie = \"3=true\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Reset\")]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[1]")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }

  @Test
  public void tEST3CATALOG() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Catalog\")]")).click();
    {
      WebElement element = driver.findElement(By.xpath("//ol/li[2]/img"));
      String attribute = element.getAttribute("src");
      vars.put("src", attribute);
    }
    assertEquals(vars.get("src").toString(), "http://localhost:8080/images/cat2.jpg");
  }

  @Test
  public void tEST4LISTING() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Catalog\")]")).click();
    vars.put("items", driver.findElements(By.xpath("//div[@id=\"listing\"]/ul/li")).size());
    assertEquals(vars.get("items").toString(), "3");
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }

  @Test
  public void tEST5RENTACAT() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Rent-A-Cat\")]")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(text(), \"Rent\")]"));
      assert (elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(text(), \"Return\")]"));
      assert (elements.size() > 0);
    }
  }

  @Test
  public void tEST7RETURN() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    js.executeScript("document.cookie = \"2=true\"");
    driver.findElement(By.xpath("//a[contains(text(), \"Rent-A-Cat\")]")).click();
    driver.findElement(By.xpath("//input[@id=\"returnID\"]")).sendKeys("2");
    driver.findElement(By.xpath("//button[contains(text(), \"Return\")]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[1]")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"returnResult\"]")).getText(), is("Success!"));
  }

  @Test
  public void tEST6RENT() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Rent-A-Cat\")]")).click();
    driver.findElement(By.xpath("//input[@id=\"rentID\"]")).sendKeys("1");
    driver.findElement(By.xpath("//button[contains(text(), \"Rent\")]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[1]")).getText(), is("Rented out"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"listing\"]/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
    assertThat(driver.findElement(By.xpath("//div[@id=\"rentResult\"]")).getText(), is("Success!"));
  }

  @Test
  public void tEST8FEEDACAT() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Feed-A-Cat\")]")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(text(), \"Feed\")]"));
      assert (elements.size() > 0);
    }
  }

  @Test
  public void tEST9FEED() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Feed-A-Cat\")]")).click();
    driver.findElement(By.xpath("//input[@id=\"catnips\"]")).sendKeys("6");
    driver.findElement(By.xpath("//button[contains(text(), \"Feed\")]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\"feedResult\"]")).getText(), is("Nom, nom, nom."));
  }

  @Test
  public void tEST10GREETACAT() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.findElement(By.xpath("//a[contains(text(), \"Greet-A-Cat\")]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\"greeting\"]")).getText(), is("Meow!Meow!Meow!"));
  }

  @Test
  public void tEST11GREETACATWITHNAME() {
    driver.get("http://localhost:8080/");
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=false\";document.cookie = \"3=false\";");
    driver.get("http://localhost:8080/greet-a-cat/Jennyanydots");
    assertThat(driver.findElement(By.xpath("//div[@id=\"greeting\"]")).getText(), is("Meow! from Jennyanydots."));
  }
}