package Task_Mng.task_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Task_Mng.task_backend.model.Task;
import Task_Mng.task_backend.model.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(TaskStatus status);
}
