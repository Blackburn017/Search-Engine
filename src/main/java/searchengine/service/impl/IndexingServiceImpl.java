package searchengine.service.impl;

import searchengine.settings.Index;
import searchengine.service.IndexingService;
import searchengine.service.responses.FalseResponseService;
import searchengine.service.responses.ResponseService;
import searchengine.service.responses.TrueResponseService;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

@Service
public class IndexingServiceImpl implements IndexingService {

    private final Index index;

    private static final Log log = LogFactory.getLog(IndexingServiceImpl.class);

    public IndexingServiceImpl(Index index) {
        this.index = index;
    }

    @Override
    public ResponseService startIndexingAll() {
        ResponseService response;
        boolean indexing;
        try {
            indexing = index.allSiteIndexing();
            log.info("Попытка запуска индексации всех сайтов");
        } catch (InterruptedException e) {
            response = new FalseResponseService("Ошибка запуска индексации");
            log.error("Ошибка запуска индексации", e);
            return response;
        }
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация всех сайтов запущена");
        } else {
            response = new FalseResponseService("Индексация уже запущена");
            log.warn("Индексация всех сайтов не запущена. Т.к. процесс индексации был запущен ранее.");
        }
        return response;
    }

    @Override
    public ResponseService stopIndexing() {
        boolean indexing = index.stopSiteIndexing();
        log.info("Попытка остановки индексации");
        ResponseService response;
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация остановлена");
        } else {
            response = new FalseResponseService("Индексация не запущена");
            log.warn("Остановка индексации не может быть выполнена, потому что процесс индексации не запущен.");
        }
        return response;
    }

    @Override
    public ResponseService startIndexingOne(String url) {
        ResponseService resp;
        String response;
        try {
            response = index.checkedSiteIndexing(url);
        } catch (InterruptedException e) {
            resp = new FalseResponseService("Ошибка запуска индексации");
            return resp;
        }

        if (response.equals("not found")) {
            resp = new FalseResponseService("Страница находится за пределами сайтов," +
                    " указанных в конфигурационном файле");
        }
        else if (response.equals("false")) {
            resp = new FalseResponseService("Индексация страницы уже запущена");
        }
        else {
            resp = new TrueResponseService();
        }
        return resp;
    }
}
