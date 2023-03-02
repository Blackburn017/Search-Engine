package searchengine.repository;

import searchengine.models.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    Page findByPath (String path);

    Optional<Page> findByIdAndSiteId (int id, int siteId);

    @Query(value = "SELECT count(*) from Page where site_id = :id")
    long count(@Param("id") long id);
}