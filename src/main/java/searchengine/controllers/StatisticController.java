package searchengine.controllers;

import searchengine.service.StatisticService;
import searchengine.service.responses.StatisticResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticController {

    private final StatisticService statistic;

    public StatisticController(StatisticService statistic) {
        this.statistic = statistic;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistics(){
        StatisticResponseService stat = statistic.getStatistic();
        return ResponseEntity.ok (stat);
    }
}
