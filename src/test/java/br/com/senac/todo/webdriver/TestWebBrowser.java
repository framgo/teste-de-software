package br.com.senac.todo.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class TestWebBrowser {

    private WebDriver driver;
    private WebElement searchBox;

    private WebElement searchButton;

    private Actions actions;

    private WebElement searchResults;

    @BeforeEach
    public void openBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    void closeBrowser() {
        //driver.quit();
    }

    @Test
    void search(){
        driver.get("https://www.google.com.br");

        searchBox = driver.findElement(By.cssSelector("[name='q']"));
        searchBox.sendKeys("Senac RJ");

        driver.manage().window().maximize();
        searchBox.submit();

        searchResults = driver.findElement(By.cssSelector("#search"));

        assertTrue(searchResults.isDisplayed());
        assertThat(driver.getTitle().startsWith("Senac Rio"));
    }


    @Test
    void searchSenacRJ(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.rj.senac.br/");
        driver.manage().window().maximize();

        searchBox = driver.findElement(By.id("bt-opensearch"));
        searchBox.click();

        searchBox = driver.findElement(By.cssSelector("[name='s']"));
        searchBox.sendKeys("Técnico em Informática");
        searchBox.submit();

        searchBox = driver.findElement(By.className("thumb-busca"));
        searchBox.click();

        searchResults = driver.findElement(By.className("turma-item"));

        assertTrue(searchResults.isDisplayed());
        assertThat(driver.getTitle().startsWith("Turma 2022.3"));
    }

    @Test
    // professor deixou o codigo com mais detalhes.
    void searchSenacRJEX03(){
        driver.get("https://www.rj.senac.br/");

        searchButton = driver.findElement(By.id("bt-opensearch"));

        actions = new Actions(driver);
        actions.moveToElement(searchButton).click().perform();

        searchBox = new WebDriverWait(driver, Duration.of(20, ChronoUnit.SECONDS)).until(ExpectedConditions.elementToBeClickable(By.id("search-field")));
        searchBox.sendKeys("Técnico de informática");
        searchBox.submit();

        searchButton = driver.findElement(By.className("thumb-busca"));
        actions.moveToElement(searchButton).click().perform();

        var turma = driver.findElement(By.cssSelector("h1[class='fw-bold color-2-1 flt-left']")).getText();

        assertEquals("Turma 2022.3", turma);

        // xpath -> linguagem de consulta de nós de um documento XML
        //var turma = driver.findElement(By.xpath("//h1[@class='fw-bold color-2-1 flt-left']")).getText();

        //var turma = driver.findElement(By.cssSelector("h1[class='fw-bold color-2-1 flt-left']")).getAttribute("innerHTML");

        //var nomeCompleto = driver.findElement(By.name("input[name='nome_completo']")).getAttribute("value");
    }
}
