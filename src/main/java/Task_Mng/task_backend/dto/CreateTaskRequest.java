package Task_Mng.task_backend.dto;

import java.time.LocalDate;

import Task_Mng.task_backend.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
		@NotBlank @Size(max = 255) String title,
		@Size(max = 2000) String description,
		TaskStatus status,
		LocalDate dueDate) {
}
