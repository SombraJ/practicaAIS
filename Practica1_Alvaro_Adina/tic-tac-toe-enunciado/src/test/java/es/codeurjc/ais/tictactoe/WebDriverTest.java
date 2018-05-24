package es.codeurjc.ais.tictactoe;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class WebDriverTest {

		protected WebDriver driver1;
		protected WebDriver driver2;
		
		@BeforeClass
		public static void setupClass() {
			System.setProperty("webdriver.chrome.driver",
					"F:\\ADE + Ing. Informatica\\4º CURSO\\2 CUATRI\\Ampliacion Software\\"
							+ "práctica 1\\tic-tac-toe-enunciado\\src\\test\\java\\es\\codeurjc\\ais\\"
							+ "tictactoe\\chromedriver.exe");
			WebApp.start();
		}
		
		@AfterClass
		public static void teardownClass() {
			WebApp.stop();
		}
		
		@Before
		public void setupTest() {
			driver1 = new ChromeDriver();
			driver2 = new ChromeDriver();
			driver1.get("http://localhost:8080/");
			driver2.get("http://localhost:8080/");
			
			driver1.findElement(By.id("nickname")).sendKeys("Calisto");
			driver1.findElement(By.id("startBtn")).click();

			driver2.findElement(By.id("nickname")).sendKeys("Melibea");
			driver2.findElement(By.id("startBtn")).click();
		}
		
		@After
		public void teardown() {
				if (driver1 != null) {
				driver1.quit();
				}
				if (driver2 != null) {
				driver2.quit();
				}
		    }
		
		@Test
		public void jugador1Gana() throws InterruptedException {
			
			
			driver1.findElement(By.id("cell-0")).click();
			driver2.findElement(By.id("cell-2")).click();
			driver1.findElement(By.id("cell-3")).click();
			driver2.findElement(By.id("cell-8")).click();
			driver1.findElement(By.id("cell-6")).click();

			
			//Mensaje alerta: Calisto Wins! Melibea looses. 
			String res1 = "Calisto wins! Melibea looses.";
			String res2 = "Calisto wins! Melibea looses.";

			Thread.sleep(3000);
			String mensajeAlerta1 = driver1.switchTo().alert().getText();

			Thread.sleep(3000);
			String mensajeAlerta2 = driver2.switchTo().alert().getText();
			
			assertEquals(res1 , mensajeAlerta1);
			assertEquals(res2 , mensajeAlerta2);
			
		}
		
		@Test
		public void jugador2Gana() throws InterruptedException{
			
			driver1.findElement(By.id("cell-0")).click();
			driver2.findElement(By.id("cell-2")).click();
			driver1.findElement(By.id("cell-1")).click();
			driver2.findElement(By.id("cell-5")).click();
			driver1.findElement(By.id("cell-4")).click();
			driver2.findElement(By.id("cell-8")).click();
			
			//Mensaje alerta: Melibea Wins! Calisto looses. 
			String res1 = "Melibea wins! Calisto looses.";
			String res2 = "Melibea wins! Calisto looses.";

			Thread.sleep(3000);
			String mensajeAlerta1 = driver1.switchTo().alert().getText();

			Thread.sleep(3000);
			String mensajeAlerta2 = driver2.switchTo().alert().getText();
			
			assertEquals(res1 , mensajeAlerta1);
			assertEquals(res2 , mensajeAlerta2);
			
		}
		
		@Test
		public void empate() throws InterruptedException{
			
			
			driver1.findElement(By.id("cell-0")).click();
			driver2.findElement(By.id("cell-1")).click();
			driver1.findElement(By.id("cell-2")).click();
			driver2.findElement(By.id("cell-3")).click();
			driver1.findElement(By.id("cell-5")).click();
			driver2.findElement(By.id("cell-4")).click();
			driver1.findElement(By.id("cell-6")).click();
			driver2.findElement(By.id("cell-8")).click();
			driver1.findElement(By.id("cell-7")).click();
			
			//Mensaje alerta: Draw!. 
			String res1 = "Draw!";
			String res2 = "Draw!";

			Thread.sleep(3000);
			String mensajeAlerta1 = driver1.switchTo().alert().getText();

			Thread.sleep(3000);
			String mensajeAlerta2 = driver2.switchTo().alert().getText();
			
			assertEquals(res1 , mensajeAlerta1);
			assertEquals(res2 , mensajeAlerta2);
			
		}
		

}








