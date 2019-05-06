package service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;
import com.sun.rowset.CachedRowSetImpl;

import dao.GradeDao;
import dao.impl.GradeDaoImpl;
import entity.Grade;
import entity.Student;
import entity.SubTask;
import entity.dto.GradeDto;
import entity.dto.StudentGradingDto;
import entity.dto.TaskDto;
import service.GradeService;
import service.StudentService;
import service.TaskService;
import tools.DBUtil;

public class GradeServiceImpl implements GradeService {
	StudentService ss = new StudentServiceImpl();
	GradeDao gd = new GradeDaoImpl();
	TaskService ts = new TaskServiceImpl();
	@Override
	public List<StudentGradingDto> getGradingList(int classId) {
		List<Integer> subTaskIdList = new ArrayList<Integer>();
		for(TaskDto t : ts.getTaskList(classId)) {
    		List<SubTask> subTaskList = t.getSubTaskList();
    		int i = 0;
    		for(SubTask subtask : subTaskList) {
    			subTaskIdList.add(subtask.getSubTaskId());
    		}
    	}
		List<Student> sl = ss.getStudentList(classId);
		 List<StudentGradingDto> sgl = new ArrayList<StudentGradingDto>();
		for (Student s : sl) {
			try {
				gradeInit(s, subTaskIdList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StudentGradingDto sgDto = new StudentGradingDto(s, gd.getGrade("select * from grade where stu_id = ?", s.getStuId()));
			sgl.add(sgDto);
		}
		return sgl;
	}
	@Override
	public List<GradeDto> getGrades(int stuId) {
		return gd.getGradeOfStudent("select grade_id,score,sub_task.sub_task_id sub_task_id,sub_task.weight,"
				+ "stu_id,task_name,sub_task_name from grade inner join sub_task on grade.sub_task_id=sub_task.sub_task_id "
				+ "inner join task on sub_task.task_id=task.task_id where grade.stu_id = ?", stuId);
	}
	@Override
	public boolean giveGrade(int stuId, int subTaskID,double score) {
		String sql="INSERT INTO grade (sub_task_id, score, stu_id)  VALUES (?, ?, ?);";
		Object[] params = {subTaskID,score,stuId};
		return gd.insert(sql, params);
	}
	@Override
	public boolean changeGrade(Grade g) {
		String sql = "UPDATE grade SET score = ? WHERE grade_id = ?;";
		Object[] params = { g.getScore(),g.getGradeId() };
		return gd.update(sql, params);
	}
	void gradeInit(Student s,List<Integer> subTL) throws SQLException{
		for(int subTaskId : subTL) {
			Object[] params = {s.getStuId(),subTaskId};
			CachedRowSet crs = DBUtil.select("select * from grade where stu_id = ? and sub_task_id = ?", params);
			if(!crs.next()) {
				giveGrade(s.getStuId(), subTaskId,0.0);
			}
		}
	}
	
}
