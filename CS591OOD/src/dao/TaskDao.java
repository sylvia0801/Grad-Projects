package dao;
import java.util.List;

import entity.SubTask;
import entity.Task;
import entity.dto.TaskDto;

public interface TaskDao extends BaseDao{
	public List<TaskDto> getTaskList(String sql,int classId);
	public List<SubTask> getSubTaskList(String sql,int taskId);
	public int addTaskAndGetId(String sql,Object[] params);
}
