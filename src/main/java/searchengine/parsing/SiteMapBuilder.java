package searchengine.parsing;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


public class SiteMapBuilder {

    private final String url;
    private final boolean isInterrupted;
    private List<String> siteMap;

    public SiteMapBuilder(String url, boolean isInterrupted){
        this.url = url;
        this.isInterrupted = isInterrupted;
    }

    public void builtSiteMap() {
        String text = new ForkJoinPool().invoke(new ParseUrl(url, isInterrupted));
        siteMap = stringToList(text);
    }

    private List<String> stringToList (String text) {
        return Arrays.stream(text.split("\n")).collect(Collectors.toList());
    }

    public List<String> getSiteMap() {
        return siteMap;
    }
}
