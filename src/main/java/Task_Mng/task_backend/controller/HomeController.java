package Task_Mng.task_backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class HomeController {

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String homePage() {
		return """
				<!DOCTYPE html>
				<html lang="en">
				<head>
				  <meta charset="UTF-8">
				  <meta name="viewport" content="width=device-width, initial-scale=1.0">
				  <title>Task Backend</title>
				  <style>
				    body { font-family: system-ui, sans-serif; max-width: 640px; margin: 3rem auto; padding: 0 1rem; color: #1a1a1a; }
				    h1 { color: #16a34a; }
				    a { color: #2563eb; }
				    ul { line-height: 1.8; }
				  </style>
				</head>
				<body>
				  <h1>Task Backend is live</h1>
				  <p>The REST API is running and ready to accept requests.</p>
				  <ul>
				    <li><a href="/swagger-ui.html">Swagger UI</a> — explore and test endpoints</li>
				    <li><a href="/api/tasks">Tasks API</a> — list all tasks</li>
				    <li><a href="/api">API info</a> — service overview</li>
				    <li><a href="/health">Health check</a> — application status</li>
				  </ul>
				</body>
				</html>
				""";
	}
}
