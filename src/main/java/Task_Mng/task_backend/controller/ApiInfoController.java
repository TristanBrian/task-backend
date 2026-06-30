package Task_Mng.task_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Task_Mng.task_backend.util.HtmlPages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "API Info", description = "Service metadata and useful links")
public class ApiInfoController {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get API entry point and documentation links")
	public Map<String, String> getApiInfo() {
		return Map.of(
				"service", "task-backend",
				"version", "1.0.0",
				"tasks", "/api/tasks",
				"swaggerUi", "/swagger-ui.html",
				"openApi", "/v3/api-docs",
				"health", "/actuator/health");
	}

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getApiInfoHtml() {
		String body = """
				<h1>Task Backend API</h1>
				<p class="muted">The service is live. Use the links below to explore endpoints.</p>
				<ul>
				  <li><a href="/api/tasks">Tasks</a> — list and manage tasks</li>
				  <li><a href="/swagger-ui.html">Swagger UI</a> — interactive API docs</li>
				  <li><a href="/v3/api-docs">OpenAPI JSON</a> — API contract</li>
				  <li><a href="/health">Health</a> — application status</li>
				  <li><a href="/actuator/health">Actuator health (JSON)</a></li>
				</ul>
				<p class="muted">JSON clients should call <code>/api</code> with <code>Accept: application/json</code>.</p>
				""";

		return HtmlPages.layout("API - Task Backend", body);
	}
}
