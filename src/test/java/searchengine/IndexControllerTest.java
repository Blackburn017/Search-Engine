package searchengine;

import searchengine.controllers.IndexController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndexController controller;


    @Test
    public void startIndexingAllTest() throws Exception {
        this.mockMvc.perform(get("/api/startIndexing").header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                .andDo(print())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void stopIndexingAllTest() throws Exception {
        this.mockMvc.perform(get("/api/stopIndexing").header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                .andDo(print())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    public void startIndexingOneTest() throws Exception {
        this.mockMvc.perform(post("/api/indexPage")
                        .param("url", "http://localhost:8080/test")
                        .header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                .andDo(print())
                .andExpect(content().string(containsString("false")));
    }
}
