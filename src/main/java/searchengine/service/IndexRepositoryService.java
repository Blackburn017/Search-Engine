package searchengine.service;

import searchengine.models.Indexing;

import java.util.List;

public interface IndexRepositoryService {
    List<Indexing> getAllIndexingByLemmaId(int lemmaId);
    List<Indexing> getAllIndexingByPageId(int pageId);
    void deleteAllIndexing(List<Indexing> indexingList);
    Indexing getIndexing (int lemmaId, int pageId);
    void save(Indexing indexing);

}
