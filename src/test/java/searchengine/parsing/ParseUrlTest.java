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

class ParseUrlTest {
//    @Mock
//    ParseUrl parseUrl;

    @Test
    void computeTestInterrupted() {
        ParseUrl parseUrl = new ParseUrl("https://jsoup.org/", true);
        assertEquals("", parseUrl.compute());
    }

    @Test
    void computeTest() throws IOException, InterruptedException {
        Document document = Jsoup.parse(new File("C:\\Users\\Lenovo\\IdeaProjects\\SearchEngine-Skillbox-\\src\\main\\resources\\templates\\testpage.html"), "UTF-8");
        String url = "https://jsoup.org/";

        ParseUrl parseUrl = new ParseUrl(url, false);

        Mockito.doReturn(document)
                .when(Mockito.mock(ParseUrl.class))
                .getDocumentByUrl(url);

        MatcherAssert.assertThat(parseUrl.compute(), Matchers.containsString("dsfdfafdsf"));
    }
}