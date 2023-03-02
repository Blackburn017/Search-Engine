package searchengine.service.impl;

import searchengine.models.Indexing;
import searchengine.repository.IndexRepository;
import searchengine.service.IndexRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexRepositoryService {

    private final IndexRepository indexRepository;

    public IndexServiceImpl(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    @Override
    public List<Indexing> getAllIndexingByLemmaId(int lemmaId) {
        return indexRepository.findByLemmaId(lemmaId);
    }

    @Override
    public List<Indexing> getAllIndexingByPageId(int pageId) {
        return indexRepository.findByPageId(pageId);
    }

    @Override
    public synchronized void deleteAllIndexing(List<Indexing> indexingList){
        indexRepository.deleteAll(indexingList);
    }

    @Override
    public Indexing getIndexing(int lemmaId, int pageId) {
        Indexing indexing = null;
        try{
            indexing = indexRepository.findByLemmaIdAndPageId(lemmaId, pageId);
        } catch (Exception e) {
            System.out.println("lemmaId: " + lemmaId + " + pageId: " + pageId + " not unique");
            e.printStackTrace();
        }
        return indexing;
    }

    @Override
    public synchronized void save(Indexing indexing) {
        indexRepository.save(indexing);
    }

}
