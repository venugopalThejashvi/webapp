package com.neu.cloud;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.sql.DataSource;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class CloudApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DataSource dataSource;

	@BeforeAll
	public static void connectToDataBase(){
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
	}


	@Test
	public void healthGetSuccess() throws Exception {
		assertEquals(200,
				mockMvc.perform
								(MockMvcRequestBuilders.get("/healthz"))
						.andDo(MockMvcResultHandlers.print())
						.andReturn()
						.getResponse()
						.getStatus());
	}

	@Test
	public void healthGetWithParams() throws Exception {
		assertEquals(400, mockMvc.perform(MockMvcRequestBuilders.get("/healthz").param("test","test")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void healthGetWithBody() throws Exception {
		String jsonBody = "{\"key\":\"value\"}";
		assertEquals(400, mockMvc.perform(MockMvcRequestBuilders.get("/healthz").contentType(MediaType.APPLICATION_JSON).content(jsonBody)).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void healthPostMethod() throws Exception {
		assertEquals(405, mockMvc.perform(MockMvcRequestBuilders.post("/healthz")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void healthPutMethod() throws Exception {
		assertEquals(405, mockMvc.perform(MockMvcRequestBuilders.put("/healthz")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void healthPatchMethod() throws Exception {
		assertEquals(405, mockMvc.perform(MockMvcRequestBuilders.patch("/healthz")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void healthDeleteMethod() throws Exception {
		assertEquals(405, mockMvc.perform(MockMvcRequestBuilders.delete("/healthz")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

	@Test
	public void checkNotFoundMethod() throws Exception {
		assertEquals(404, mockMvc.perform(MockMvcRequestBuilders.get("/abc")).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getStatus());
	}

}
