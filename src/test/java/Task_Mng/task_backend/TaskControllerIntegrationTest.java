package Task_Mng.task_backend;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	void apiInfoReturnsHtmlForBrowser() throws Exception {
		mockMvc.perform(get("/api").accept("text/html"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Task Backend API")));
	}

	@Test
	void tasksReturnHtmlForBrowser() throws Exception {
		mockMvc.perform(get("/api/tasks").accept("text/html"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Tasks")));
	}

	@Test
	void healthPageIsLive() throws Exception {
		mockMvc.perform(get("/health").accept("text/html"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Application Health")));
	}

	@Test
	void homePageIsLive() throws Exception {
		mockMvc.perform(get("/").accept("text/html"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Task Backend is live")));
	}

	@Test
	void healthEndpointIsLive() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("UP")));
	}

	@Test
	void openApiDocsAreAvailable() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title", is("Task Management API")));
	}

	@Test
	void swaggerUiIsAvailable() throws Exception {
		mockMvc.perform(get("/swagger-ui.html"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void apiInfoEndpointIsLive() throws Exception {
		mockMvc.perform(get("/api"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.service", is("task-backend")))
				.andExpect(jsonPath("$.tasks", is("/api/tasks")));
	}

	@Test
	void taskCrudEndpointsWork() throws Exception {
		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		String createPayload = """
				{
				  "title": "Integration test task",
				  "description": "Created by automated test",
				  "status": "TODO",
				  "dueDate": "2026-07-01"
				}
				""";

		MvcResult createResult = mockMvc.perform(post("/api/tasks")
						.contentType(APPLICATION_JSON)
						.content(createPayload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title", is("Integration test task")))
				.andExpect(jsonPath("$.status", is("TODO")))
				.andReturn();

		JsonNode createdTask = OBJECT_MAPPER.readTree(createResult.getResponse().getContentAsString());
		long taskId = createdTask.get("id").asLong();

		mockMvc.perform(get("/api/tasks/" + taskId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is((int) taskId)));

		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(put("/api/tasks/" + taskId)
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "status": "IN_PROGRESS"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("IN_PROGRESS")));

		mockMvc.perform(delete("/api/tasks/" + taskId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/tasks/" + taskId))
				.andExpect(status().isNotFound());
	}
}
