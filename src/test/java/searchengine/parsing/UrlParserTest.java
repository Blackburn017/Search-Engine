package searchengine.parsing;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlParserTest {
//    @Mock
//    ParseUrl parseUrl;

    @Test
    void computeTestInterrupted() {
        UrlParser urlParser = new UrlParser("https://jsoup.org/", true);
        assertEquals("", urlParser.compute());
    }

    @Test
    void computeTest() throws IOException, InterruptedException {
        Document document = Jsoup.parse(new File("C:\\Users\\Lenovo\\IdeaProjects\\SearchEngine-Skillbox-\\src\\main\\resources\\templates\\testpage.html"), "UTF-8");
        String url = "https://jsoup.org/";

        UrlParser urlParser = new UrlParser(url, false);

        Mockito.doReturn(document)
                .when(Mockito.mock(UrlParser.class))
                .getDocumentByUrl(url);

        MatcherAssert.assertThat(urlParser.compute(), Matchers.containsString("dsfdfafdsf"));
    }
}