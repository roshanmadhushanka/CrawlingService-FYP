package main;

public class CrawlerTest {
    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        System.out.println(crawler.crawl("https://github.com/SeleniumHQ/selenium/issues/3534"));
    }
}
