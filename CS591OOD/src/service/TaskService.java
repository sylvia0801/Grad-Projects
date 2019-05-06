package service;

import java.util.List;

import entity.Student;
import entity.SubTask;
import entity.Task;
import entity.dto.TaskDto;

public interface TaskService {
	public List<TaskDto> getTaskList(int classId);
	public int addTask(Task t);
	public boolean addSubTask(SubTask t);
	public boolean editTask(Task t);
	public boolean editSubTask(SubTask st);
	public boolean deleteTask(Task t);
	public boolean deleteSubTask(SubTask st);
}
