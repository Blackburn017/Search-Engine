package searchengine.service.impl;

import searchengine.models.Page;
import searchengine.repository.PageRepository;
import searchengine.service.PageRepositoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageServiceImpl implements PageRepositoryService {

    private final PageRepository pageRepository;

    public PageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    public Page getPage(String pagePath) {
        return pageRepository.findByPath(pagePath);
    }

    @Override
    public synchronized void save(Page page) {
        pageRepository.save(page);
    }

    @Override
    public Optional<Page> findPageById(int id) {
        return pageRepository.findById(id);
    }

    @Override
    public Optional<Page> findPageByPageIdAndSiteId(int pageId, int siteId) {
        return pageRepository.findByIdAndSiteId(pageId, siteId);
    }

    @Override
    public long pageCount(){
        return pageRepository.count();
    }

    @Override
    public long pageCount(long siteId){
        return pageRepository.count(siteId);
    }

    @Override
    public void deletePage(Page page) {
        pageRepository.delete(page);
    }

}
