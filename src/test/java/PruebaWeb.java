import net.bytebuddy.asm.Advice;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;

import javax.xml.bind.SchemaOutputResolver;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;


public class PruebaWeb {
    private WebDriver firefoxDriver;
    Scanner reader = new Scanner(System.in);

    @Before
    public void doSomethingBefore(){
    }

    @Test
    public void irAUrl(){
        String URL="https://www.demoblaze.com/index.html",
                user="laxxv5", pass="laxxv5",
                nombreLaptop="Sony vaio i5";
        System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("marionette", false);
        firefoxDriver = new FirefoxDriver(options);
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        firefoxDriver.get(URL);

        Assert.assertTrue("Ingresó OK a la URL: "+URL, true);

        try{
            //registrarse(user, pass);
        }
        catch(Exception e){
            Assert.fail("Error al registrar un nuevo usuario: ya existe en el sistema");
        }
        try{
            iniciarSesion(user, pass);
        }
        catch(Exception e){
            Assert.fail("Se produjo un error iniciando la sesión");
        }
        try{
            agregarLaptop(nombreLaptop);
        }
        catch(Exception e){
            Assert.fail("Se produjo un error agregando la laptop al carrito");
        }
        try{
            verificarCarrito(nombreLaptop);
        }
        catch(Exception e){
            Assert.fail("Se produjo un error verificando el carrito");
        }


    }

    private void verificarCarrito(String nombreLaptop) {
        System.out.println("\n*****************Método verificar carrito**************\n");

        firefoxDriver.get("https://www.demoblaze.com/cart.html");
        boolean seAñadioAlCarrito=false;
        try{
            firefoxDriver.findElement(By.xpath("//td[contains(text(),'"+nombreLaptop+"')]"));
            seAñadioAlCarrito = true;
        }
        catch(Exception e){
            System.out.println("No se añadió al carrito");
        }
        //System.out.println("El valor del booleano es: "+seAñadioAlCarrito);
        Assert.assertTrue("El producto no se añadió al carrito", seAñadioAlCarrito);

        System.out.println("---> OK");
    }

    private void agregarLaptop(String nombreLaptop) {
        System.out.println("\n*****************Método agregar laptop**************\n");

        //Selecciono la categoría laptop, elijo la primera y la agrego al carrito
        WebElement laptops = firefoxDriver.findElement(By.xpath("//a[contains(text(),'Laptops')]"));
        laptops.click();


        //String urlProducto = firefoxDriver.findElement(By.xpath("//a[contains(text(),'Sony vaio i5')]")).getAttribute("href");
        String urlProducto = firefoxDriver.findElement(By.xpath("//a[contains(text(),'"+nombreLaptop+"')]")).getAttribute("href");

        firefoxDriver.findElement(By.xpath("//a[contains(text(),'"+nombreLaptop+"')]")).click();


        firefoxDriver.get(urlProducto);

        try{
            Thread.sleep(3000);
            WebElement botonAñadir=firefoxDriver.findElement(By.xpath("//a[contains(text(),'Add to cart')]"));
            botonAñadir.click();
        }
        catch(UnhandledAlertException e){
            System.out.println("Se lanzó un modal: "+e.getMessage());
        }
        catch(InterruptedException e){
            System.out.println("Error en el hilo: "+e.getMessage());
        }


        //      CERRAR EL MODAL ---
        try{
            Thread.sleep(3000);
            if(firefoxDriver.switchTo().alert() != null)
            {
                Alert alert = firefoxDriver.switchTo().alert();
                alert.dismiss();
            }
        }
        catch(Exception e){
        }
        System.out.println("---> OK");
    }

    private void registrarse(String user, String pass) throws Exception{
        //ME REGISTRO CON USUARIO Y CONTRASEÑA
        System.out.println("\n*****************Método registrarse**************\n");
        firefoxDriver.findElement(By.xpath("//a[@id='signin2']")).click();
        firefoxDriver.findElement(By.xpath("//input[@id='sign-username']")).sendKeys(user);
        firefoxDriver.findElement(By.xpath("//input[@id='sign-password']")).sendKeys(pass);
        firefoxDriver.findElement(By.xpath("//button[contains(text(),'Sign up')]")).click(); //      CERRAR EL MODAL ---
        try{
            Thread.sleep(3000);

        }
        catch(InterruptedException e){
        }
        if(firefoxDriver.switchTo().alert().getText().contains("This user already exist."))
        {
            throw new Exception();
        }
        if(firefoxDriver.switchTo().alert() != null)
        {
            Alert alert = firefoxDriver.switchTo().alert();
            alert.dismiss();
        }
        System.out.println("---> OK");
    }

    private void iniciarSesion(String user, String pass) {
        //INICIO SESIÓN
        System.out.println("\n*****************Método iniciar sesión**************\n");
        firefoxDriver.findElement(By.xpath("//a[@id='login2']")).click();
        firefoxDriver.findElement(By.xpath("//input[@id='loginusername']")).sendKeys(user);
        firefoxDriver.findElement(By.xpath("//input[@id='loginpassword']")).sendKeys(pass);

        firefoxDriver.findElement(By.xpath("//button[contains(text(),'Log in')]")).click();

        firefoxDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        try{
            Thread.sleep(3000);
        }
        catch(Exception e){
        }
        System.out.println("---> OK");
    }


    @After
    public void doSomethingAfter(){
//
        firefoxDriver.quit();
    }
}
