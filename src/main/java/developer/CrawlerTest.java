package developer;

import main.Crawler;

public class CrawlerTest {
    public static void main(String[] args) {
        Crawler crawler = Crawler.getCrawler();
        System.out.println(crawler.crawl("http://edition.cnn.com"));
    }
}
