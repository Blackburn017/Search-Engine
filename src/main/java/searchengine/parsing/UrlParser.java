package searchengine.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.commons.logging.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.RecursiveTask;

public class UrlParser extends RecursiveTask<String> {
    public final static List<String> urlList = new Vector<>();

    private final static Log log = LogFactory.getLog(UrlParser.class);
    private final String url;
    private final boolean isInterrupted;

    public UrlParser(String url, boolean isInterrupted) {
        this.url = url;
        this.isInterrupted = isInterrupted;
    }

    @Override
    protected String compute() {
        if(isInterrupted){
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(url);
        try {
            Thread.sleep(200);
            Document doc = getDocumentByUrl(url);
            Elements rootElements = doc.select("a");

            List<UrlParser> linkGrabers = new ArrayList<>();
            rootElements.forEach(element -> {
                String link = element.attr("abs:href");
                if (link.startsWith(element.baseUri())
                        && !link.equals(element.baseUri())
                        && !link.contains("#")
                        && !link.contains(".pdf")
                        && !urlList.contains(link)
                ) {
                    urlList.add(link);
                    UrlParser linkGraber = new UrlParser(link, false);
                    linkGraber.fork();
                    linkGrabers.add(linkGraber);
                }
            });

            for (UrlParser lg : linkGrabers) {
                String text = lg.join();
                if (!text.equals("")) {
                    result.append("\n");
                    result.append(text);
                }
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Ошибка парсинга сайта: " + url);
        }
        return result.toString();
    }

    protected Document getDocumentByUrl (String url) throws InterruptedException, IOException {
        Thread.sleep(200);
        return Jsoup.connect(url)
                .maxBodySize(0)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
    }
}
