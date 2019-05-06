package service.impl;

import java.util.List;

import dao.TaskDao;
import dao.impl.TaskDaoImpl;
import entity.Student;
import entity.SubTask;
import entity.Task;
import entity.dto.TaskDto;
import service.TaskService;

public class TaskServiceImpl implements TaskService {
	private TaskDao td = new TaskDaoImpl();
	@Override
	public List<TaskDto> getTaskList(int classId) {
		String sql = "select * from task where class_id = ?";
		return td.getTaskList(sql, classId);
	}
	@Override
	public int addTask(Task t) {
			String sql="INSERT INTO task (class_id, task_name, weight)  VALUES (?, ? ,?);";
			Object[] params = {t.getClassId(),t.getTaskName(),t.getWeight()};
			return td.addTaskAndGetId(sql,params);
	}
	@Override
	public boolean addSubTask(SubTask st) {
		String sql="INSERT INTO sub_task (task_id, sub_task_name, weight)  VALUES (?, ? ,?);";
		Object[] params = {st.getTaskId(),st.getSubTaskName(),st.getWeight()};
		return td.insert(sql, params);
	}
	@Override
	public boolean editTask(Task t) {
		String sql = "UPDATE task SET task_name = ? , weight = ?  WHERE task_id = ?;";
		Object[] params = { t.getTaskName(),t.getWeight(), t.getTaskId() };
		return td.update(sql, params);
	}
	@Override
	public boolean editSubTask(SubTask st) {
		String sql = "UPDATE sub_task SET sub_task_name = ? , weight = ?  WHERE sub_task_id = ?;";
		Object[] params = { st.getSubTaskName(),st.getWeight(),st.getSubTaskId() };
		return td.update(sql, params);
	}
	@Override
	public boolean deleteTask(Task t) {
		String sql = "DELETE FROM task WHERE task_id = ?;";
		Object[] params = {t.getTaskId()};
		return td.delete(sql, params);
	}
	@Override
	public boolean deleteSubTask(SubTask st) {
		String sql = "DELETE FROM sub_task WHERE sub_task_id = ?;";
		Object[] params = {st.getSubTaskId()};
		return td.delete(sql, params);
	}

}
