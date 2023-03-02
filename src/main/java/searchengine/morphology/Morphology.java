package searchengine.morphology;

import org.apache.commons.logging.*;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.WrongCharaterException;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class Morphology {
    private static final Log log = LogFactory.getLog(Morphology.class);
    private static final Set<String> parts;
    private static LuceneMorphology morphology = null;
    private final TreeMap<String, Integer> map;

    static {
        parts = new TreeSet<>();
        parts.add("ЧАСТ");
        parts.add("СОЮЗ");
        parts.add("МЕЖД");
        parts.add("ПРЕДЛ");
        try {
            morphology = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("Ошибка морфологического анализа." + e);
        }
    }

    public Morphology(){
        map = new TreeMap<>();
    }

    public TreeMap<String, Integer> textAnalyzer (String text) {
        text = text.replaceAll("[—]|\\p{Punct}", " ").toLowerCase(Locale.ROOT);
        String[] separateWords = text.split("\\s+");
        for (String word : separateWords) {
            word = word.trim();
            try {
                if (!isServiceParts(word)) {
                    List<String> words = morphology.getNormalForms(word);
                    for(String s : words) {
                        putWordsToMap(s);
                    }
                }
            } catch (Exception ex) {
                log.debug("Ошибка морфологического анализа. Пропущенные символы: " + word);
            }
        }
        return map;
    }

    public ArrayList<String> getLemmas (String text) {
        ArrayList<String> list = new ArrayList<>();
        text = text.replaceAll("[—]|\\p{Punct}", " ").toLowerCase(Locale.ROOT);
        String[] separateWords = text.split("\\s+");
        for (String word : separateWords) {
            word = word.trim();
            if (!isServiceParts(word)) {
                try {
                    List<String> words = morphology.getNormalForms(word);
                    list.add(words.get(0));
                } catch (WrongCharaterException ex) {
                    log.debug("Ошибка морфологического анализа. Пропущенные символы: " + word);
                }

            }
        }
        return list;
    }

    public ArrayList<Integer> findLemmaIndexInText(String text, String lemma) {
        ArrayList<Integer> listOfIndexes = new ArrayList<>();
        String[] list = text.split("[—]|\\p{Punct}|\\s");
        int index = 0;
        for(String s1 : list) {
            List<String> lemmas = new ArrayList<>();
            try {
                lemmas = morphology.getNormalForms(s1.toLowerCase(Locale.ROOT));

            } catch (Exception e) {
                log.debug("Ошибка морфологического анализа. Пропущенные символы: " + s1);
            }
            for(String s2 : lemmas) {
                if (s2.equals(lemma)){
                    listOfIndexes.add(index);
                }
            }
            index += s1.length() + 1;
        }
        return listOfIndexes;
    }

    private boolean isServiceParts (String word) {
        boolean is = false;
        try {
            List<String> list = morphology.getMorphInfo(word);
            for(String s : list) {
                s = s.substring(s.indexOf(" ") + 1);
                if (parts.contains(s)) {
                    is = true;
                    break;
                }
            }
        } catch (WrongCharaterException ex) {
            log.debug("Ошибка морфологического анализа. Символы не могут быть проанализированны: " + word);
        }

        return is;
    }

    private void putWordsToMap (String word) {
        if (map.containsKey(word)){
            int count = map.get(word);
            map.put(word, ++count);
        } else {
            map.put(word, 1);
        }
    }
}


