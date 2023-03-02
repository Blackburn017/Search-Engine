package searchengine.service;

import searchengine.models.Request;
import searchengine.service.responses.ResponseService;

import java.io.IOException;

public interface SearchService {
    ResponseService getResponse (Request request, String url, int offset, int limit) throws IOException;
}
