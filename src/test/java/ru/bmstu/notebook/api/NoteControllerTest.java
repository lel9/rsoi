package ru.bmstu.notebook.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.notebook.Application;
import ru.bmstu.notebook.model.NoteInPost;
import ru.bmstu.notebook.model.NoteInUpdate;
import ru.bmstu.notebook.model.NoteOut;
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
public class NoteControllerTest {
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
    public void getNotes() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/notes/ab598598-e12e-4f3d-be82-40ba87ed58ad"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("56b65658-418a-4c16-9723-384a40937f0d")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId", Matchers.is("ab598598-e12e-4f3d-be82-40ba87ed58ad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("note 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("hello 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created_at", Matchers.is(1573158948)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].modified_at", Matchers.is(1573158948)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is("8100ffac-0f55-4c37-b0c6-6e13df640c47")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId", Matchers.is("ab598598-e12e-4f3d-be82-40ba87ed58ad")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", Matchers.is("note 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text", Matchers.is("hello 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created_at", Matchers.is(1573153150)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].modified_at", Matchers.is(1573158900)));
    }

    @Test
    public void getNotesFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/notes/ab598598-e12e-4f3d-be82-40ba87ed58a1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void addNote() throws Exception {
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders
                .post("/note/create")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new NoteInPost("ab598598-e12e-4f3d-be82-40ba87ed58ad", "title", "text"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("text")))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        NoteOut noteOut = parseNote(contentAsString);

        Assert.assertEquals(noteOut.getCreated_at(), noteOut.getModified_at());
    }

    @Test
    public void addNoteFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .post("/note/create")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new NoteInPost("ab598598-e12e-4f3d-be82-40ba87ed58a1", "title", "text"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void updateNote() throws Exception {
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders
                .put("/note/update/56b65658-418a-4c16-9723-384a40937f0d")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new NoteInUpdate("title", "text"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("text")))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        NoteOut noteOut = parseNote(contentAsString);

        Assert.assertTrue(noteOut.getCreated_at() < noteOut.getModified_at());
    }

    @Test
    public void updateNoteFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .put("/note/update/56b65658-418a-4c16-9723-384a40937f01")
                .contentType(APPLICATION_JSON_UTF8)
                .content(makeRequestBody(new NoteInUpdate("title", "text"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteNote() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/note/delete/56b65658-418a-4c16-9723-384a40937f0d"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assert.assertFalse(noteRepository.findById(UUID.fromString("56b65658-418a-4c16-9723-384a40937f0d")).isPresent());
    }

    @Test
    public void deleteNoteFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .delete("/note/delete/56b65658-418a-4c16-9723-384a40937f01"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private static String makeRequestBody(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    private static NoteOut parseNote(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, NoteOut.class);
    }
}
