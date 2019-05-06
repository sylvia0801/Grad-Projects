package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dao.StudentDao;
import entity.Classes;
import entity.Student;
import tools.DBUtil;

public class StudentDaoImpl extends BaseDaoImpl implements StudentDao {

	@Override
	public List<Student> getStudentList(String sql,Object[] params) {

		List<Student> data = new ArrayList<Student>();
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while(crs.next()) {
				data.add(new Student(Integer.parseInt(crs.getString(1)), Integer.parseInt(crs.getString(4)), crs.getString(3), crs.getString(2), Integer.parseInt(crs.getString(5)) , crs.getString(6)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public Student getStudent(String sql,Object[] params) {
		Student s = null;
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while(crs.next()) {
				s = new Student(Integer.parseInt(crs.getString(1)), Integer.parseInt(crs.getString(4)), crs.getString(3), crs.getString(2), Integer.parseInt(crs.getString(5)) , crs.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

}
