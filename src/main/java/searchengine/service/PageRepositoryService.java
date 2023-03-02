package searchengine.service;

import searchengine.models.Page;

import java.util.Optional;

public interface PageRepositoryService {
    Page getPage (String pagePath);
    void save(Page page);
    Optional<Page> findPageById(int id);
    Optional<Page> findPageByPageIdAndSiteId(int pageId, int siteId);
    long pageCount();
    long pageCount(long siteId);
    void deletePage(Page page);
}
