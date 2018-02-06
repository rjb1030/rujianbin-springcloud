import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rujianbin.AppWebApplication;
import rujianbin.app.web.controller.CommonController;

import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rujianbin on 2018/2/6.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppWebApplication.class)
@WebAppConfiguration
public class ApplicationTest {

    private MockMvc mvc;

    @Before
    public void setup() throws Exception{
        mvc = MockMvcBuilders.standaloneSetup(new CommonController()).build();
    }

    @Test
    public void hello()throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/common/testResponse").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("hello world2")));
    }

}
