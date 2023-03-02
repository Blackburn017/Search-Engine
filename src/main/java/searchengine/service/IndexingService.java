package searchengine.service;

import searchengine.service.responses.ResponseService;

public interface IndexingService {
    ResponseService startIndexingAll();
    ResponseService stopIndexing();
    ResponseService startIndexingOne(String url);
}
