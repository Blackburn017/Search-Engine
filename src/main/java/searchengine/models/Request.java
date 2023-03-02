package searchengine.models;

import searchengine.morphology.Morphology;

import java.util.*;


public class Request {

    private String req;
    private List<String> reqLemmas;

    public List<String> getReqLemmas() {
        return reqLemmas;
    }

    public String getReq() {
        return req;
    }

    public Request(String req){
        this.req = req;
        reqLemmas = new ArrayList<>();
        try {
            Morphology analyzer = new Morphology();
            reqLemmas.addAll(analyzer.getLemmas(req));
        }catch (Exception e) {
            System.out.println("ошибка морфологочиского анализа");
        }
    }
}
