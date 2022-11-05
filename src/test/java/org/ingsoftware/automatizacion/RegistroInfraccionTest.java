package org.ingsoftware.automatizacion;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Properties;

import static org.junit.Assert.assertEquals;


public class RegistroInfraccionTest {

    private WebDriver webDriver;
    private static Statement stmt;


    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", ".\\src\\test\\resources\\chromedriver\\chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:3000/infraccion");
        initDatabase();
    }

    @Test
    public void debeRegistrarInfraccionSatisfactoriamente() throws SQLException {

        WebElement primeraBox = webDriver.findElement(By.id("nombretxt"));
        primeraBox.sendKeys("Yilmer Patricio");

        WebElement apellidoTxt = webDriver.findElement(By.id("apellidotxt"));
        apellidoTxt.sendKeys("Fernancoz de la Roa");

        WebElement emailTxt = webDriver.findElement(By.id("emailtxt"));
        emailTxt.sendKeys("Yilmer@aol.com");

        WebElement placaTxt = webDriver.findElement(By.id("placatxt"));
        placaTxt.sendKeys("BBE332");

        WebElement documentoTxt = webDriver.findElement(By.id("iddoctxt"));
        documentoTxt.sendKeys("25147125");

        Select infraccionSel = new Select(webDriver.findElement(By.id("inftxt")));
        infraccionSel.selectByVisibleText("84c9dcbc-bae4-4fef-aa46-77541656cf95");

        Select tipoDocumentoSel = new Select(webDriver.findElement(By.id("doctxt")));
        tipoDocumentoSel.selectByVisibleText("CC");

        WebElement enviarRegistro = webDriver.findElement(By.id("btnreg"));
        enviarRegistro.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));

        String query = "select * from infraction where vehicle_id" ;

        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next())
        {
            assertEquals("30f02214-44cf-4027-8212-5da19a4d21b9", resultSet.getString("vehicle_id"));
        }

        webDriver.quit();
    }

    private void initDatabase() {
        Properties prop = new Properties();
        try {
            prop.load(RegistroInfraccionTest.class.getClassLoader().getResourceAsStream("registro-infraccion.properties"));

            String DB_URL = prop.getProperty("database");
            String DB_USER = prop.getProperty("user");
            String DB_PASSWORD = prop.getProperty("password");
            String dbClass = "com.mysql.cj.jdbc.Driver";

            Class.forName(dbClass).newInstance();
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = con.createStatement();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


}
