package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import entity.Task;
import dao.TaskDao;
import entity.SubTask;
import entity.dto.TaskDto;
import tools.DBUtil;

public class TaskDaoImpl extends BaseDaoImpl implements TaskDao {

	@Override
	public List<TaskDto> getTaskList(String sql,int classId) {
		List<TaskDto> tdl = new ArrayList<>();
		TaskDto tDto = null;
		Object[] params = {classId};
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while(crs.next()) {
				List<SubTask> stl = new ArrayList<>();
				stl = this.getSubTaskList("select * from sub_task where task_id = ?", Integer.parseInt(crs.getString(1)));
				tDto = new TaskDto(Integer.parseInt(crs.getString(1)), classId, crs.getString(3), Double.parseDouble(crs.getString(4)), stl);
				tdl.add(tDto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tdl;
	}

	@Override
	public List<SubTask> getSubTaskList(String sql, int taskId) {
		List<SubTask> stl = new ArrayList<>();
		SubTask st = null;
		Object[] params = {taskId};
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while(crs.next()) {
				st = new SubTask(Integer.parseInt(crs.getString(1)), crs.getString(3), Integer.parseInt(crs.getString(2)), Double.parseDouble(crs.getString(4)));
				stl.add(st);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stl;
	}

	@Override
	public int addTaskAndGetId(String sql,Object[] params) {
		
		return DBUtil.insertAndGetId(sql, params);
	}


}
