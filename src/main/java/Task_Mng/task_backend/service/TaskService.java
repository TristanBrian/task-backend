package Task_Mng.task_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Task_Mng.task_backend.dto.CreateTaskRequest;
import Task_Mng.task_backend.dto.TaskResponse;
import Task_Mng.task_backend.dto.UpdateTaskRequest;
import Task_Mng.task_backend.exception.ResourceNotFoundException;
import Task_Mng.task_backend.model.Task;
import Task_Mng.task_backend.model.TaskStatus;
import Task_Mng.task_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

	private final TaskRepository taskRepository;

	@Transactional(readOnly = true)
	public List<TaskResponse> getAllTasks(TaskStatus status) {
		List<Task> tasks = status == null
				? taskRepository.findAll()
				: taskRepository.findByStatus(status);
		return tasks.stream().map(TaskResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public TaskResponse getTaskById(Long id) {
		return TaskResponse.from(findTaskOrThrow(id));
	}

	public TaskResponse createTask(CreateTaskRequest request) {
		Task task = Task.builder()
				.title(request.title())
				.description(request.description())
				.status(request.status() != null ? request.status() : TaskStatus.TODO)
				.dueDate(request.dueDate())
				.build();
		return TaskResponse.from(taskRepository.save(task));
	}

	public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
		Task task = findTaskOrThrow(id);

		if (request.title() != null) {
			task.setTitle(request.title());
		}
		if (request.description() != null) {
			task.setDescription(request.description());
		}
		if (request.status() != null) {
			task.setStatus(request.status());
		}
		if (request.dueDate() != null) {
			task.setDueDate(request.dueDate());
		}

		return TaskResponse.from(taskRepository.save(task));
	}

	public void deleteTask(Long id) {
		Task task = findTaskOrThrow(id);
		taskRepository.delete(task);
	}

	private Task findTaskOrThrow(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
	}
}
