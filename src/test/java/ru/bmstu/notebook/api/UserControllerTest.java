package ru.bmstu.notebook.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.bmstu.notebook.Application;

import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.notebook.model.UserIn;
import ru.bmstu.notebook.repository.NoteRepository;
import ru.bmstu.notebook.repository.UserRepository;
import ru.bmstu.notebook.service.UserServiceImpl;

import java.nio.charset.Charset;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MockMvc mvc;

    private static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Test
    public void getUsers() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("ab598598-e12e-4f3d-be82-40ba87ed58ad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("olga")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", Matchers.is("ya")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("ya@mail.ru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is("72c3c2cd-a431-4cd8-8eca-9e7aad115159")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("kate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname", Matchers.is("am")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.is("am@mail.ru")));
    }

    @Test
    public void getUser() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/user/ab598598-e12e-4f3d-be82-40ba87ed58ad"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("ab598598-e12e-4f3d-be82-40ba87ed58ad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("olga")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("ya")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("ya@mail.ru")));
    }

    @Test
    public void getUserFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/user/ab598598-e12e-4f3d-be82-40ba87ed58a1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getUserFailUUID() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/user/1111"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void addUser() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .post("/user/create")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new UserIn("jane", "ch", "ch@mail.ru"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("ch")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("ch@mail.ru")));
    }

    @Test
    public void updateUser() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .put("/user/update/ab598598-e12e-4f3d-be82-40ba87ed58ad")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new UserIn("jane", "ch", "ch@mail.ru"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("jane")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("ch")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("ch@mail.ru")));
    }

    @Test
    public void updateUserFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .put("/user/update/ab598598-e12e-4f3d-be82-40ba87ed58a1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteUser() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/user/delete/ab598598-e12e-4f3d-be82-40ba87ed58ad"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assert.assertFalse(userRepository.findById(UUID.fromString("ab598598-e12e-4f3d-be82-40ba87ed58a1")).isPresent());
        Assert.assertFalse(noteRepository.findById(UUID.fromString("8100ffac-0f55-4c37-b0c6-6e13df640c47")).isPresent());
        Assert.assertFalse(noteRepository.findById(UUID.fromString("56b65658-418a-4c16-9723-384a40937f0d")).isPresent());
    }

    @Test
    public void deleteUserFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/user/delete/ab598598-e12e-4f3d-be82-40ba87ed58a1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private static String makeRequestBody(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }
}
