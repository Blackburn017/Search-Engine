package searchengine.repository;

import searchengine.models.Lemma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LemmaRepository extends CrudRepository<Lemma, Integer> {
    List<Lemma> findByLemma (String lemma);

    @Query(value = "SELECT * from search_lemma WHERE id IN(:id)", nativeQuery = true)
    List<Lemma> findById (int[] id);

    @Query(value = "SELECT count(*) from Lemma where site_id = :id")
    long count(@Param("id") long id);
}