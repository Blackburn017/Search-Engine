package searchengine.morphology;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeMap;

class MorphologyAnalyzerTest {

    MorphologyAnalyzer analyzer = new MorphologyAnalyzer();
    String testText = "Почему бы нам не начать анализировать этот замечательный тестовый текст, ведь его так интересно анализировать.";

    @Test
    void textAnalyzer() {
        TreeMap<String, Integer> map = analyzer.textAnalyzer(testText);
        MatcherAssert.assertThat(map, Matchers.hasEntry("анализировать",2));
        MatcherAssert.assertThat(map, Matchers.hasEntry("текст",1));
    }

    @Test
    void getLemmas() {
        MatcherAssert.assertThat(analyzer.getLemmas(testText), Matchers.hasItem("замечательный"));
    }

    @Test
    void findLemmaIndexInText() {
        ArrayList<Integer> list = analyzer.findLemmaIndexInText(testText, "анализировать");
        MatcherAssert.assertThat(list, Matchers.hasItem(24));
        MatcherAssert.assertThat(list, Matchers.hasItem(96));
    }
}