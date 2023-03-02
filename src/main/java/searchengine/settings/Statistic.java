package searchengine.settings;

import searchengine.models.Site;
import searchengine.models.Status;
import searchengine.service.LemmaRepositoryService;
import searchengine.service.PageRepositoryService;
import searchengine.service.SiteRepositoryService;
import searchengine.service.StatisticService;
import searchengine.service.Entity.Detailed;
import searchengine.service.Entity.Statistics;
import searchengine.service.Entity.Total;
import searchengine.service.responses.StatisticResponseService;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Statistic implements StatisticService {

    private static final Log log = LogFactory.getLog(Statistic.class);

    private final SiteRepositoryService siteRepositoryService;
    private final LemmaRepositoryService lemmaRepositoryService;
    private final PageRepositoryService pageRepositoryService;

    public Statistic(SiteRepositoryService siteRepositoryService,
                     LemmaRepositoryService lemmaRepositoryService,
                     PageRepositoryService pageRepositoryService) {
        this.siteRepositoryService = siteRepositoryService;
        this.lemmaRepositoryService = lemmaRepositoryService;
        this.pageRepositoryService = pageRepositoryService;
    }

    public StatisticResponseService getStatistic(){
        Total total = getTotal();
        List<Site> siteList = siteRepositoryService.getAllSites();
        Detailed[] detaileds = new Detailed[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            detaileds[i] = getDetailed(siteList.get(i));
        }
        log.info("Получение статистики.");
        return new StatisticResponseService(true, new Statistics(total, detaileds));
    }

    private Total getTotal(){
        long sites = siteRepositoryService.siteCount();
        long lemmas = lemmaRepositoryService.lemmaCount();
        long pages = pageRepositoryService.pageCount();
        boolean isIndexing = isSitesIndexing();
        return new Total(sites, pages, lemmas, isIndexing);

    }

    private Detailed getDetailed(Site site){
        String url = site.getUrl();
        String name = site.getName();
        Status status = site.getStatus();
        long statusTime = site.getStatusTime().getTime();
        String error = site.getLastError();
        long pages = pageRepositoryService.pageCount(site.getId());
        long lemmas = lemmaRepositoryService.lemmaCount(site.getId());
        return new Detailed(url, name, status, statusTime, error, pages, lemmas);
    }

    private boolean isSitesIndexing(){
        boolean is = true;
        for(Site s : siteRepositoryService.getAllSites()){
            if(!s.getStatus().equals(Status.INDEXED)){
                is = false;
                break;
            }
        }
    return is;
    }
}
