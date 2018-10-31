package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

    @Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private JavaScriptFrameworkRepository repository;

	private void prepareData() throws Exception {
		JavaScriptFramework react = new JavaScriptFramework("ReactJS");
		JavaScriptFramework vue = new JavaScriptFramework("Vue.js");
		
		repository.save(react);
		repository.save(vue);
	}

	@Test
	public void frameworksTest() throws Exception {
		prepareData();

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Vue.js")));
	}
	
	@Test
	public void addFrameworkInvalid() throws Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));
		
		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors", hasSize(1)))
			.andExpect(jsonPath("$.errors[0].field", is("name")))
			.andExpect(jsonPath("$.errors[0].message", is("Size")));
		
	}

	@Test
	public void addFramework() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("hello world");
        Date date = new Date();
        framework.setDeprecationDate(date);
        framework.setHypeLevel(60);
        String [] arr = {"1.0", "2.1"};
        framework.setVersion(arr);
        mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(jsonPath("$.name", is("hello world")))
                .andExpect(jsonPath("$.deprecationDate", is(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date))))
                .andExpect(jsonPath("$.hypeLevel", is(60)))
                .andExpect(jsonPath("$.version", hasSize(arr.length)))
                .andExpect(jsonPath("$.version[0]", is(arr[0])))
                .andExpect(jsonPath("$.version[1]", is(arr[1])));
	}

    @Test
    public void deleteFramework() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework("to delete");
        framework = repository.save(framework);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delete/{id}", framework.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)));
        assert !repository.findById(framework.getId()).isPresent();
	}

    @Test
    public void updateFramework() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework("to edit");
        framework = repository.save(framework);
        framework.setName("after editing");
        mockMvc.perform((MockMvcRequestBuilders
                .put("/update/{id}", framework.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework))))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(framework.getName())));
    }
	
}
