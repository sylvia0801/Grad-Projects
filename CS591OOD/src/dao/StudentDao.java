package dao;

import java.util.List;

import entity.Student;

public interface StudentDao extends BaseDao {
	public List<Student> getStudentList(String sql, Object[] params);
	public Student getStudent(String sql,Object[] params);
}
