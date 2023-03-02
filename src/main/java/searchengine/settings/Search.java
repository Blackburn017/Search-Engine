package searchengine.settings;

import searchengine.morphology.MorphologyAnalyzer;
import searchengine.service.IndexRepositoryService;
import searchengine.service.LemmaRepositoryService;
import searchengine.service.PageRepositoryService;
import searchengine.service.SiteRepositoryService;
import searchengine.service.responses.SearchResponseService;
import searchengine.service.Entity.SearchData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import searchengine.models.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class Search {

    private final SiteRepositoryService siteRepositoryService;
    private final IndexRepositoryService indexRepositoryService;
    private final PageRepositoryService pageRepositoryService;
    private final LemmaRepositoryService lemmaRepositoryService;

    public Search(SiteRepositoryService siteRepositoryService,
                  IndexRepositoryService indexRepositoryService,
                  PageRepositoryService pageRepositoryService,
                  LemmaRepositoryService lemmaRepositoryService) {
        this.siteRepositoryService = siteRepositoryService;
        this.indexRepositoryService = indexRepositoryService;
        this.pageRepositoryService = pageRepositoryService;
        this.lemmaRepositoryService = lemmaRepositoryService;
    }

    public SearchResponseService searchService (Request request, String url, int offset, int limit) {
        List<Site> siteList = siteRepositoryService.getAllSites();
        List<SearchData> listOfSearchData = new ArrayList<>();
        if(url == null) {
            for(Site s : siteList){
                Map<Page, Double> list = searching(request, s.getId());

                listOfSearchData.addAll(getSortedSearchData(list, request));
            }
        } else {
            Site site = siteRepositoryService.getSite(url);
            Map<Page, Double> list = searching(request, site.getId());
            listOfSearchData.addAll(getSortedSearchData(list, request));
        }
        int count;
        listOfSearchData.sort(Comparator.comparingDouble(SearchData::getRelevance));
        if (listOfSearchData.isEmpty()){
            return new SearchResponseService(false);
        }
        if(limit + offset < listOfSearchData.size()) {
            count = limit;
        } else {
            count = listOfSearchData.size() - offset;
        }
        SearchData[] searchData = new SearchData[count];
        for (int i = offset; i < count; i++) {
            searchData[i] = listOfSearchData.get(i);
        }

        return new SearchResponseService(true, count, searchData);
    }

    private Map<Page, Double> searching(Request request, int siteId) {
        HashMap<Page, Double> pageRelevance = new HashMap<>();
        List<Lemma> reqLemmas = sortedReqLemmas(request, siteId);
        List<Integer> pageIndexes = new ArrayList<>();
        if (!reqLemmas.isEmpty()) {
            List<Indexing> indexingList = indexRepositoryService.getAllIndexingByLemmaId(reqLemmas.get(0).getId());
            indexingList.forEach(indexing -> pageIndexes.add(indexing.getPageId()));
            for (Lemma lemma : reqLemmas) {
                if (!pageIndexes.isEmpty() && lemma.getId() != reqLemmas.get(0).getId()) {
                    List<Indexing> indexingList2 = indexRepositoryService.getAllIndexingByLemmaId(lemma.getId());
                    List<Integer> tempList = new ArrayList<>();
                    indexingList2.forEach(indexing -> tempList.add(indexing.getPageId()));
                    pageIndexes.retainAll(tempList);
                }
            }
            Map<Page, Double> pageAbsRelevance = new HashMap<>();

            double maxRel = 0.0;
            for (Integer p : pageIndexes) {
                Optional<Page> opPage;
                opPage = pageRepositoryService.findPageByPageIdAndSiteId(p, siteId);
                if (opPage.isPresent()) {
                    Page page = opPage.get();
                    double r = getAbsRelevance(page, reqLemmas);
                    pageAbsRelevance.put(page, r);
                    if (r > maxRel)
                        maxRel = r;
                }
            }
            for (Map.Entry<Page, Double> abs : pageAbsRelevance.entrySet()) {
                pageRelevance.put(abs.getKey(), abs.getValue() / maxRel);
            }
        }

        return pageRelevance;
    }

    private List<SearchData> getSortedSearchData (Map<Page, Double> sortedPageMap, Request request) {
        List<SearchData> responses = new ArrayList<>();
        LinkedHashMap<Page, Double> sortedByRankPages = (LinkedHashMap<Page, Double>) sortMapByValue(sortedPageMap);
        for (Map.Entry<Page, Double> page : sortedByRankPages.entrySet()) {
            SearchData response = getResponseByPage(page.getKey(), request, page.getValue());
            responses.add(response);
        }
        return responses;
    }

    private List<Lemma> sortedReqLemmas(Request request, int siteId){
        List<Lemma> lemmaList = new ArrayList<>();
        List<String> list = request.getReqLemmas();
        for(String s : list) {
            List<Lemma> reqLemmas = lemmaRepositoryService.getLemma(s);
            for(Lemma l : reqLemmas){
                if(l.getSiteId() == siteId){
                    lemmaList.add(l);
                }
            }
        }
        lemmaList.sort(Comparator.comparingInt(Lemma::getFrequency));
        return lemmaList;
    }

    private double getAbsRelevance(Page page, List<Lemma> lemmas){
        double r = 0.0;
        int pageId = page.getId();
        for (Lemma lemma : lemmas) {
            int lemmaId = lemma.getId();
            Indexing indexing = indexRepositoryService.getIndexing(lemmaId, pageId);
            r = r + indexing.getRank();
        }
        return r;
    }


    private SearchData getResponseByPage (Page page, Request request, double relevance) {
        SearchData response = new SearchData();
        Site site = siteRepositoryService.getSite(page.getSiteId());
        String siteUrl = site.getUrl();
        String siteName = site.getName();
        String uri = page.getPath();
        String title = getTitle(page.getContent());
        String snippet = getSnippet(page.getContent(), request);
        response.setSite(siteUrl);
        response.setSiteName(siteName);
        response.setRelevance(relevance);
        response.setUri(uri);
        response.setTitle(title);
        response.setSnippet(snippet);
        return response;
    }

    private String getTitle (String html){
        String string = "";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("title");
        StringBuilder builder = new StringBuilder();
        elements.forEach(element -> builder.append(element.text()).append(" "));
        if (!builder.isEmpty()){
            string = builder.toString();
        }
        return string;
    }

    private String getSnippet (String html, Request request) {
        MorphologyAnalyzer analyzer = new MorphologyAnalyzer();
        String string = "";
        Document document = Jsoup.parse(html);
        Elements titleElements = document.select("title");
        Elements bodyElements = document.select("body");
        StringBuilder builder = new StringBuilder();
        titleElements.forEach(element -> builder.append(element.text()).append(" ").append("\n"));
        bodyElements.forEach(element -> builder.append(element.text()).append(" "));
        if (!builder.isEmpty()){
            string = builder.toString();
        }
        List<String> req = request.getReqLemmas();
        Set<Integer> integerList = new TreeSet<>();
        for (String s : req) {
            integerList.addAll(analyzer.findLemmaIndexInText(string, s));
        }
        List<TreeSet<Integer>> indexesList = getSearchingIndexes(string, integerList);
        StringBuilder builder1 = new StringBuilder();
        for (TreeSet<Integer> set : indexesList) {
            int from = set.first();
            int to = set.last();
            Pattern pattern = Pattern.compile("\\p{Punct}|\\s");
            Matcher matcher = pattern.matcher(string.substring(to));
            int offset = 0;
            if (matcher.find()){
                offset = matcher.end();
            }
            builder1.append("<b>")
                    .append(string, from, to + offset)
                    .append("</b>");
            if (!((string.length() - to) < 30)) {
                builder1.append(string, to + offset, string.indexOf(" ", to + offset + 30))
                        .append("... ");
            }
        }
        return builder1.toString();
    }

    private List<TreeSet<Integer>> getSearchingIndexes (String string, Set<Integer> indexesOfBolt) {
        ArrayList<Integer> indexes = new ArrayList<>(indexesOfBolt);
        List<TreeSet<Integer>> list = new ArrayList<>();
        TreeSet<Integer> temp = new TreeSet<>();
        for (int i = 0; i < indexes.size(); i++) {
            String s = string.substring(indexes.get(i));
            int end = s.indexOf(" ");
            if ((i + 1) <= indexes.size() - 1 && (indexes.get(i + 1) - indexes.get(i)) < end + 5){
                temp.add(indexes.get(i));
                temp.add(indexes.get(i + 1));
            }
             else {
                if (!temp.isEmpty()) {
                    list.add(temp);
                    temp = new TreeSet<>();
                }
                temp.add(indexes.get(i));
                list.add(temp);
                temp = new TreeSet<>();
            }
        }
        list.sort((Comparator<Set<Integer>>) (o1, o2) -> o2.size() - o1.size());
        ArrayList<TreeSet<Integer>> searchingIndexes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if(list.size() > i) {
                searchingIndexes.add(list.get(i));
            }
        }
        return searchingIndexes;
    }

    /* thanks VeLKerr from stackoverflow for sortMapByValue */
    public  <K, V extends Comparable<? super V>> Map<K, V>
    sortMapByValue(Map<K, V> map )
    {
        Map<K,V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K,V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e ->result.put(e.getKey(),e.getValue()));

        return result;
    }
}
