package searchengine.service.responses;


import searchengine.dto.Statistics;

public class StatisticResponseService implements ResponseService {
    boolean result;
    Statistics statistics;

    public StatisticResponseService(boolean result, Statistics statistics) {
        this.result = result;
        this.statistics = statistics;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
