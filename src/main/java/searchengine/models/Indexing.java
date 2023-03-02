package searchengine.models;

import javax.persistence.*;

@Entity
@Table(name="Search_index")
public class Indexing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "page_id")
    private int pageId;
    @Column(name = "lemma_id")
    private int lemmaId;
    private float ranking;

    public Indexing() {
    }

    public Indexing(int pageId, int lemmaId, float ranking) {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.ranking = ranking;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getLemmaId() {
        return lemmaId;
    }

    public void setLemmaId(int lemmaId) {
        this.lemmaId = lemmaId;
    }

    public float getRank() {
        return ranking;
    }

    public void setRank(float rank) {
        this.ranking = rank;
    }

    @Override
    public String toString() {
        return "Indexing{" +
                "pageId=" + pageId +
                ", lemmaId=" + lemmaId +
                ", ranking=" + ranking +
                '}';
    }
}
