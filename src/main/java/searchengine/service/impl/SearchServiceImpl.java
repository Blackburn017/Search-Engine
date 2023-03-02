package searchengine.service.impl;

import searchengine.settings.Search;
import searchengine.models.Request;
import searchengine.service.SearchService;
import searchengine.service.responses.FalseResponseService;
import searchengine.service.responses.ResponseService;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Log log = LogFactory.getLog(SearchServiceImpl.class);

    private final Search search;

    public SearchServiceImpl(Search search) {
        this.search = search;
    }

    ResponseService response;

    @Override
    public ResponseService getResponse(Request request, String url, int offset, int limit) throws IOException {
        log.info("Запрос на поиск строки- \"" + request.getReq() + "\"");
        if (request.getReq().equals("")){
            response = new FalseResponseService("Задан пустой поисковый запрос");
            log.warn("Задан пустой поисковый запрос");
            return response;
            }
        if(url.equals("")) {
            response = search.searchService(request, null, offset, limit);
        } else {
            response = search.searchService(request, url, offset, limit);
        }
        if (response.getResult()) {
            log.info("Запрос на поиск строки обработан, результат получен.");
            return response;
        } else {
            log.warn("Запрос на поиск строки обработан, указанная страница не найдена.");
            return new FalseResponseService("Указанная страница не найдена");
        }
    }
}
