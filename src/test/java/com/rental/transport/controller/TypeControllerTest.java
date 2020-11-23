package com.rental.transport.controller;

import com.rental.transport.BaseResourceTest;
import com.rental.transport.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
public class TypeControllerTest extends BaseResourceTest {

    @Mock
    private TypeService typeService;

    @Test
    public void doGetCountTypeRequest() throws Exception {

        Mockito
                .doReturn(10L)
                .when(typeService).count();

        getMockMvc().perform(MockMvcRequestBuilders.get("/type/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
//                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    public void doGetListTypeRequest() throws Exception {
        getMockMvc().perform(MockMvcRequestBuilders.get("/type/list?page=0&size=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

/*
    @Test
    public void doPostTypeRequest() throws Exception {
        this.getMockMvc().perform(MockMvcRequestBuilders.post("/type?name=est"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
*/
}