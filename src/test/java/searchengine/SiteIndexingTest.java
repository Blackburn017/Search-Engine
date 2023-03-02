package searchengine;

import searchengine.models.Site;
import com.egortroyan.searchengine.service.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import searchengine.service.*;
import searchengine.settings.SearchSettings;

@RunWith(SpringRunner.class)
@SpringBootTest
class SiteIndexingTest {

    @MockBean
    private SearchSettings searchSettings;
    @MockBean
    private FieldRepositoryService fieldRepositoryService;
    @MockBean
    private SiteRepositoryService siteRepositoryService;
    @MockBean
    private IndexRepositoryService indexRepositoryService;
    @MockBean
    private PageRepositoryService pageRepositoryService;
    @MockBean
    private LemmaRepositoryService lemmaRepositoryService;
    Site site = new Site();

    @Test
    void runOneSiteIndexing() {
    }
}