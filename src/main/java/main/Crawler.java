package main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by roshanalwis on 9/28/17.
 */
public class Crawler {
    WebDriver driver;
    public Crawler() {
        DesiredCapabilities desireCaps = new DesiredCapabilities();
        desireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                System.getProperty("user.dir") + "/src/main/resources/phantomjs");
        desireCaps.setCapability("phantomjs.cli.args", Collections.singletonList("--ignore-ssl-errors=true"));
        desireCaps.setCapability("phantomjs.page.settings.userAgent",
                "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");

        driver = new PhantomJSDriver(new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File(System.getProperty("user.dir") + "/src/main/resources/phantomjs"))
                .withLogFile(null).build(),
                desireCaps);
    }

    public String crawl(String url) {
        driver.get(url);
        return driver.getPageSource();
    }

    public void close() {
        if(driver != null) {
            driver.close();
        }
    }
}
