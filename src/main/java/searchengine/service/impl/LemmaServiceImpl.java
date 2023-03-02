package searchengine.service.impl;

import searchengine.models.Indexing;
import searchengine.models.Lemma;
import searchengine.repository.LemmaRepository;
import searchengine.service.LemmaRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LemmaServiceImpl implements LemmaRepositoryService {

    private final LemmaRepository lemmaRepository;

    public LemmaServiceImpl(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }

    @Override
    public List<Lemma> getLemma(String lemmaName) {
        List<Lemma> lemmas = null;
        try{
            lemmas = lemmaRepository.findByLemma(lemmaName);
        } catch (Exception e) {
            System.out.println(lemmaName);
            e.printStackTrace();
        }
        return lemmas;
    }

    @Override
    public synchronized void save(Lemma lemma) {
        lemmaRepository.save(lemma);
    }

    @Override
    public long lemmaCount(){
        return lemmaRepository.count();
    }

    @Override
    public long lemmaCount(long siteId){
        return lemmaRepository.count(siteId);
    }

    @Override
    public synchronized void deleteAllLemmas(List<Lemma> lemmaList){
        lemmaRepository.deleteAll(lemmaList);
    }

    @Override
    public List<Lemma> findLemmasByIndexing(List<Indexing> indexingList){
        int[] lemmaIdList = new int[indexingList.size()];
        for (int i = 0; i < indexingList.size(); i++) {
            lemmaIdList[i] = indexingList.get(i).getLemmaId();
        }
        return lemmaRepository.findById(lemmaIdList);
    }
}
