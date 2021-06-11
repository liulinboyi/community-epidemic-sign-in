package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/applicationContext.xml", "classpath:config/springmvc-config.xml"})
@WebAppConfiguration
public class ItemMockTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/findCustomerById").param("id", "1"))
                .andExpect(MockMvcResultMatchers.view().name("customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assert.assertNotNull(result.getModelAndView().getModel().get("customer"));
    }

    @Test
    public void test2() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/registration").param("id", "6"))
                .andExpect(MockMvcResultMatchers.view().name("registration"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assert.assertNotNull(result.getModelAndView().getModel().get("registration"));
    }

    /**
     * 测试json
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/testJson")
//                .param("id", "1")
                .content("{\"id\":6,\"username\":\"小明\"}")
                .header("content-type", "application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("小明"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void test3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/findUserSign")
                .param("id", "1")
                .header("content-type", "application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("小明"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testLogin() throws Exception {
        Cookie cookie = new Cookie("1", "{id=1,+username='xiaoming',+nickname='小明',+telephone='31231233',+birthday='null',+gender=1,+address='河北省',+idcard='130234567890123456',+password='null',+signs=null}");
        Cookie cookie1 = new Cookie("time", "1622716197186");
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                .param("id", "1")
//                .content("{\"username\":\"xiaoming\",\"password\":\"1234567890\"}")
//                .content("{\"username\":\"xiaoming\",\"password\":\"123456789\"}")
//                .content("")
                .header("content-type", "application/json")
//                .cookie(cookie)
//                .cookie(cookie1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("小明"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
