package Task_Mng.task_backend.dto;

import java.time.Instant;
import java.time.LocalDate;

import Task_Mng.task_backend.model.Task;
import Task_Mng.task_backend.model.TaskStatus;

public record TaskResponse(
		Long id,
		String title,
		String description,
		TaskStatus status,
		LocalDate dueDate,
		Instant createdAt,
		Instant updatedAt) {

	public static TaskResponse from(Task task) {
		return new TaskResponse(
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				task.getStatus(),
				task.getDueDate(),
				task.getCreatedAt(),
				task.getUpdatedAt());
	}
}
