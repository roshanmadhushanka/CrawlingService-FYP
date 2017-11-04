package main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.jna.platform.win32.WinDef;
import org.apache.commons.lang3.SystemUtils;
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
    private WebDriver driver;
    public static Crawler crawler = null;

    private String getDriverPath() {
        // File separator changed based on the operating system
        String separator = File.separator;
        String driverPath = System.getProperty("user.dir") + "/src/main/resources/PhantomJSDriver/";

        // Select path based on specific platform
        if(SystemUtils.IS_OS_MAC) {
            driverPath += "/mac/phantomjs";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            driverPath += "/windows/phantomjs.exe";
        } else if (SystemUtils.IS_OS_LINUX) {
            driverPath += "/ubuntu/phantomjs";
        }

        // Replace common separator
        driverPath = driverPath.replace("/", separator);
        return driverPath;
    }


    private Crawler() {
        // Setup browser capabilities
        DesiredCapabilities desireCaps = new DesiredCapabilities();
        desireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                getDriverPath());
        desireCaps.setCapability("phantomjs.cli.args", Collections.singletonList("--ignore-ssl-errors=true"));
        desireCaps.setCapability("phantomjs.page.settings.userAgent",
                "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");

        // Initiate driver
        driver = new PhantomJSDriver(new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File(getDriverPath()))
                .withLogFile(null).build(),
                desireCaps);
    }

    public static Crawler getCrawler() {
        /*
            Return static crawler object
         */
        if(crawler == null){
            crawler = new Crawler();
        }

        return crawler;
    }


    public String crawl(String url) {
        /*
            Crawl web page and return content as a string
         */

        driver.get(url);
        return driver.getPageSource();
    }
}
