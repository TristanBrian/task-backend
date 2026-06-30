package Task_Mng.task_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Task_Mng.task_backend.dto.CreateTaskRequest;
import Task_Mng.task_backend.dto.TaskResponse;
import Task_Mng.task_backend.dto.UpdateTaskRequest;
import Task_Mng.task_backend.model.TaskStatus;
import Task_Mng.task_backend.service.TaskService;
import Task_Mng.task_backend.util.HtmlPages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

	private final TaskService taskService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all tasks", description = "Optionally filter tasks by status")
	public List<TaskResponse> getAllTasks(@RequestParam(required = false) TaskStatus status) {
		return taskService.getAllTasks(status);
	}

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getAllTasksHtml(@RequestParam(required = false) TaskStatus status) {
		List<TaskResponse> tasks = taskService.getAllTasks(status);

		StringBuilder rows = new StringBuilder();
		for (TaskResponse task : tasks) {
			rows.append("""
					<tr>
					  <td><a href="/api/tasks/%d">%d</a></td>
					  <td>%s</td>
					  <td>%s</td>
					  <td>%s</td>
					  <td>%s</td>
					</tr>
					""".formatted(
					task.id(),
					task.id(),
					HtmlPages.escape(task.title()),
					HtmlPages.escape(task.description()),
					task.status(),
					task.dueDate() != null ? task.dueDate() : "-"));
		}

		String filterNote = status != null
				? "<p class=\"muted\">Filtered by status: <strong>" + status + "</strong></p>"
				: "";

		String body = """
				<h1>Tasks</h1>
				%s
				<p><a href="/api/tasks?status=TODO">TODO</a> · <a href="/api/tasks?status=IN_PROGRESS">In progress</a> · <a href="/api/tasks?status=DONE">Done</a> · <a href="/api/tasks">All</a></p>
				<table>
				  <thead>
				    <tr><th>ID</th><th>Title</th><th>Description</th><th>Status</th><th>Due date</th></tr>
				  </thead>
				  <tbody>
				    %s
				  </tbody>
				</table>
				<p class="muted">Use <a href="/swagger-ui.html">Swagger UI</a> to create, update, or delete tasks.</p>
				""".formatted(filterNote, rows);

		return HtmlPages.layout("Tasks - Task Backend", body);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get a task by ID")
	public TaskResponse getTaskById(@PathVariable Long id) {
		return taskService.getTaskById(id);
	}

	@GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public String getTaskByIdHtml(@PathVariable Long id) {
		TaskResponse task = taskService.getTaskById(id);

		String body = """
				<h1>Task #%d</h1>
				<table>
				  <tr><th>Title</th><td>%s</td></tr>
				  <tr><th>Description</th><td>%s</td></tr>
				  <tr><th>Status</th><td>%s</td></tr>
				  <tr><th>Due date</th><td>%s</td></tr>
				  <tr><th>Created</th><td>%s</td></tr>
				  <tr><th>Updated</th><td>%s</td></tr>
				</table>
				<p><a href="/api/tasks">← Back to all tasks</a></p>
				""".formatted(
				task.id(),
				HtmlPages.escape(task.title()),
				HtmlPages.escape(task.description()),
				task.status(),
				task.dueDate() != null ? task.dueDate() : "-",
				task.createdAt(),
				task.updatedAt());

		return HtmlPages.layout("Task " + task.id(), body);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new task")
	public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
		return taskService.createTask(request);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an existing task")
	public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
		return taskService.updateTask(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a task")
	public void deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
	}
}
