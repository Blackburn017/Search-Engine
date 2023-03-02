package searchengine.service;

import searchengine.models.Site;

import java.util.List;

public interface SiteRepositoryService {
    Site getSite (String url);
    Site getSite (int siteId);
    void save(Site site);
    long siteCount();
    List<Site> getAllSites();
}
