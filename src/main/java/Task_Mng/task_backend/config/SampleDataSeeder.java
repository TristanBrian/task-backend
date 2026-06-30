package Task_Mng.task_backend.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import Task_Mng.task_backend.model.Task;
import Task_Mng.task_backend.model.TaskStatus;
import Task_Mng.task_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "app.seed-sample-data", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class SampleDataSeeder implements ApplicationRunner {

	private final TaskRepository taskRepository;

	@Override
	public void run(ApplicationArguments args) {
		if (taskRepository.count() > 0) {
			return;
		}

		taskRepository.saveAll(List.of(
				Task.builder()
						.title("Set up project")
						.description("Initialize Spring Boot backend with PostgreSQL")
						.status(TaskStatus.DONE)
						.dueDate(LocalDate.now().minusDays(1))
						.build(),
				Task.builder()
						.title("Test API in Swagger")
						.description("Open Swagger UI and try the task endpoints")
						.status(TaskStatus.IN_PROGRESS)
						.dueDate(LocalDate.now().plusDays(2))
						.build(),
				Task.builder()
						.title("Connect frontend")
						.description("Wire up the UI to the live REST endpoints")
						.status(TaskStatus.TODO)
						.dueDate(LocalDate.now().plusDays(7))
						.build()));

		log.info("Seeded {} sample tasks for local testing", 3);
	}
}
