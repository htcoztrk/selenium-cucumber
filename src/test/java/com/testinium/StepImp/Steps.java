package com.testinium.StepImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.testinium.Hook.Hooks;
import com.testinium.helper.ElementHelper;
import com.testinium.helper.StoreHelper;
import com.testinium.model.ElementInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class Steps {

    private Logger logger = LoggerFactory.getLogger(getClass());
    public WebDriver webDriver;
    public Actions actions;
    public WebDriverWait webDriverWait;
    private final int timeOut = 30;
    private final int sleepTime = 150;
    private static String SAVED_ATTRIBUTE;
    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;


    public Steps() {
        this.webDriver = Hooks.getWebDriver();
        this.webDriverWait = new WebDriverWait(webDriver, timeOut, sleepTime);
        this.actions = new Actions(webDriver);
    }

    private void hover(String key) {

        actions.moveToElement(findElement(key));

    }

    private void clickElement(String key) {
        hover(key);
        findElement(key).click();
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", element);
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }


    private void sendKeyESC(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by) {
        return webDriver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return webDriver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }

    WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return webDriver.findElements(infoParam);
    }

    @Given("{string} elementine {string} degerini yaz")
    public void elementineDegeriniYaz(String key, String text) {
        findElement(key).sendKeys(text);
    }

    @And("{string} elementine {string} key gonder")
    public void elementinekeygonder(String key, String text) {
        findElement(key).sendKeys(Keys.valueOf(text));
    }

    @Then("{string} elementinin bulunduğunu kontrol et")
    public void elementininBulunduğunuKontrolEt(String key) {
        Assert.assertTrue("Element Görünür durumda değil", findElement(key).isDisplayed());

    }

    @Given("{string} elementine tıkla")
    public void elementineTıkla(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            waitByMilliSeconds(500);
            clickElement(key);
            logger.info(key + " elementine tıklandı.");

        }
    }



    @Then("{int} saniye bekle")
    public void saniyeBekle(int second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("{string} sayfasına git")
    public void sayfasınaGit(String uri) {
        webDriver.get(uri);
    }

    @Given("Elementi bekle ve sonra tıkla {string}")
    public void checkElementExistsThenClick(String key) {
        getElementWithKeyIfExists(key);
        clickElement(key);
        logger.info(key + " elementine tıklandı.");
    }

    @Given("Elementin yüklenmesini bekle {string}")
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }

    @Given("{long} milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Given("Element {string} var mı kontrol et yoksa hata mesajı ver {string}")
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(message);
    }

    @Given("Element yok mu kontrol et {string}")
    public void checkElementNotExists(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element '" + key + "' still exist.");
    }

    @Given("Proje içindeki <path> dosyayı {string} elemente upload et")
    public void uploadFile(String path, String key) {
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyası " + key + " elementine yüklendi.");
    }

    @Given("{string} textini {string} elemente yaz")
    public void ssendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    @Given("Javascript ile css tıkla {string}")
    public void javascriptClickerWithCss(String css) {
        assertTrue("Element bulunamadı", isDisplayedBy(By.cssSelector(css)));
        javaScriptClicker(webDriver, webDriver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " tıklandı.");
    }

    @Given("Javascript ile xpath tıkla {string}")
    public void javascriptClickerWithXpath(String xpath) {
        assertTrue("Element bulunamadı", isDisplayedBy(By.xpath(xpath)));
        javaScriptClicker(webDriver, webDriver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " tıklandı.");
    }

    @Given("Şuanki URL {string} değerini içeriyor mu kontrol et")
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = webDriver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Given("Elemente TAB keyi yolla {string}")
    public void sendKeyToElementTAB(String key) {
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yollandı.");
    }

    @Given("Elemente BACKSPACE keyi yolla {string}")
    public void sendKeyToElementBACKSPACE(String key) {
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandı.");
    }

    @Given("Elemente ESCAPE keyi yolla {string}")
    public void sendKeyToElementESCAPE(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yollandı.");
    }

    @Given("{string} elementi {string} niteliğine sahip mi")
    public void checkElementAttributeExists(String key, String attribute) {
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Given("{string} elementi {string} niteliğine sahip değil mi")
    public void checkElementAttributeNotExists(String key, String attribute) {
        WebElement element = findElement(key);

        int loopCount = 0;

        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Given("{string} elementinin {string} niteliği {string} değerine sahip mi")
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't match expected value");
    }

    @Given("{string} elementinin {string} niteliği {string} değerini içeriyor mu")
    public void checkElementAttributeContains(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't contain expected value");
    }

    @Given("{string} değerini {string} niteliğine {string} elementi için yaz")
    public void setElementAttribute(String value, String attributeName, String key) {
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    @Given("{string} değerini {string} niteliğine {string} elementi için JS ile yaz")
    public void setElementAttributeWithJs(String value, String attributeName, String key) {
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
                webElement);
    }

    @Given("{string} elementinin text alanını temizle")
    public void clearInputArea(String key) {
        findElement(key).clear();
    }

    @Given("{string} niteliğini sakla {string} elementi için")
    public void saveAttributeValueOfElement(String attribute, String key) {
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    @Given("Kaydedilmiş niteliği {string} elementine yaz")
    public void writeSavedAttributeToElement(String key) {
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    @Given("{string} elementi {string} değerini içeriyor mu kontrol et")
    public void checkElementContainsText(String key, String expectedText) {

        Boolean containsText = findElement(key).getText().contains(expectedText);
        assertTrue("Expected text is not contained", containsText);
        logger.info(key + " elementi" + expectedText + "değerini içeriyor.");
    }

    @Given("{string} elementine random değer yaz")
    public void writeRandomValueToElement(String key) {
        findElement(key).sendKeys(randomString(15));
    }

    @Given("{string} elementine {string} değeri ile başlayan random değer yaz")
    public void writeRandomValueToElement(String key, String startingText) {
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    @Given("Elementin text değerini yazdır css {string}")
    public void printElementText(String css) {
        System.out.println(webDriver.findElement(By.cssSelector(css)).getText());
    }


    @Given("Sayfayı yenile")
    public void refreshPage() {
        webDriver.navigate().refresh();
    }


    @Given("Sayfanın zoom değerini değiştir {string}%")
    public void chromeZoomOut(String value) {
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    @Given("Yeni sekme aç")
    public void chromeOpenNewTab() {
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
    }

    @Given("<number> numaralı sekmeye odaklan")//Starting from 1
    public void chromeFocusTabWithNumber(int number) {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(number - 1));
    }

    @Given("Son sekmeye odaklan")
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Given("Frame'e odaklan {string}")
    public void chromeFocusFrameWithNumber(String key) {
        WebElement webElement = findElement(key);
        webDriver.switchTo().frame(webElement);
    }

    @Given("Chrome uyarı popup'ını kabul et")
    public void acceptChromeAlertPopup() {
        webDriver.switchTo().alert().accept();
    }

    @Given("{string} olarak {int} indexi seçersem")
    public void choosingIndexFromDemandNo(String key, int index) {

        List<WebElement> anchors = findElements(key);
        WebElement anchor = anchors.get(index);
        anchor.click();
    }

    @Given("Excel dosya indirme kontrolu")
    public void  delte_file() {
        String home = System.getProperty("user.home");
        String file_name = "invoicing.xlsx";
        String file_with_location = home + "/Downloads/" + file_name;
        System.out.println("Function Name ===========================" + home + "/Downloads/"+ file_name);
        File file = new File(file_with_location);
        if (file.exists()) {
            System.out.println(file_with_location + " is present");
            file.delete();
            if (file.delete()) {
                System.out.println("file deleted");
            } else {
                System.out.println("file not deleted");
            }
        } else {
            System.out.println(file_with_location + " is not present");
        }
    }


}
