package com.asgrim.cleverbotselfchat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

public class CleverbotSelfChat {
    public static void main(String[] args) throws IOException {
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("/usr/local/bin/chromedriver"))
                .usingAnyFreePort()
                .build();
        service.start();

        WebDriver a = openBrowser(service);
        WebDriver b = openBrowser(service);

        System.out.print("<< ");
        sendMessage(a, "Hello");

        for (int responses = 0; responses <= 4; responses++) {
            System.out.print(">> ");
            respond(a, b);
            System.out.print("<< ");
            respond(b, a);
        }

        service.stop();
    }

    private static WebDriver openBrowser(ChromeDriverService service)
    {
        WebDriver driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        driver.manage().window().setSize(new Dimension(800, 600));
        driver.get("https://www.cleverbot.com/");
        return driver;
    }

    private static void sendMessage(WebDriver recipient, String message)
    {
        WebElement input = recipient.findElement(By.name("stimulus"));
        input.sendKeys(message);
        input.submit();

        System.out.println(message);
    }

    private static String readLastMessage(WebDriver messageFrom)
    {
        WebDriverWait wait = new WebDriverWait(messageFrom, 10);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("snipTextIcon")));

        WebElement snipTextIcon = messageFrom.findElement(By.id("snipTextIcon"));
        WebElement responseElement = snipTextIcon.findElement(By.xpath("preceding-sibling::*[1]"));
        return responseElement.getText();
    }

    private static void respond(WebDriver them, WebDriver me)
    {
        sendMessage(me, readLastMessage(them));
    }
}
