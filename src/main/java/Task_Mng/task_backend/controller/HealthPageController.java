package Task_Mng.task_backend.controller;

import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Task_Mng.task_backend.util.HtmlPages;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Hidden
public class HealthPageController {

	private final HealthEndpoint healthEndpoint;

	@GetMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
	public String healthPage() {
		String status = healthEndpoint.health().getStatus().getCode();
		String statusClass = "UP".equals(status) ? "status-up" : "";

		String body = """
				<h1>Application Health</h1>
				<p>Status: <span class="%s">%s</span></p>
				<p class="muted">Machine-readable health data is available at <a href="/actuator/health">/actuator/health</a>.</p>
				""".formatted(statusClass, HtmlPages.escape(status));

		return HtmlPages.layout("Health - Task Backend", body);
	}
}
