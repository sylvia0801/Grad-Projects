package service.impl;

import java.util.List;

import dao.StudentDao;
import dao.impl.StudentDaoImpl;
import entity.Student;
import service.StudentService;
import tools.CsvUtil;

public class StudentServiceImpl implements StudentService {
	private StudentDao sd = new StudentDaoImpl();

	@Override
	public List<Student> getStudentList(int classId) {
		Object[] params = { classId };
		return sd.getStudentList("select * from student where class_id = ?", params);
	}

	@Override
	public Student getStudent(int stuId) {
		Object[] params = { stuId };
		return sd.getStudent("select * from student where stu_id = ?", params);
	}

	@Override
	public boolean editStudent(Student s) {
		String sql = "UPDATE student SET last_name = ? , first_name = ? ,stu_type = ?, class_id = ? WHERE stu_id = ?;";
		Object[] params = { s.getLastName(), s.getFirstName(), s.getStuType(), s.getClassId(), s.getStuId() };
		return sd.update(sql, params);
	}

	@Override
	public boolean deleteStudent(Student s) {
		String sql = "DELETE FROM student WHERE stu_id = ?;";
		Object[] params = { s.getStuId() };
		return sd.delete(sql, params);
	}

	@Override
	public boolean addStudent(Student s) {
		if (!checkIfExist(s)) {
			String sql = "INSERT INTO student (last_name, first_name, class_id, stu_type,bu_id)  VALUES (?, ?, ? ,?,?);";
			Object[] params = { s.getLastName(), s.getFirstName(), s.getClassId(), s.getStuType(), s.getBuId() };
			return sd.insert(sql, params);
		} else {
			System.out.println("Student:" + s.getBuId() + " already exists");
			return false;
		}

	}

	boolean checkIfExist(Student s) {
		Object[] params = { s.getBuId() };
		return s.equals(sd.getStudent("select * from student where bu_id = ?", params));
	}

	@Override
	public boolean LoadStudentFromCsv(String filepath, int classId) {
		try {
			CsvUtil.LoadCsv(filepath, classId);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<Student> searchStudent(String queryString,int classId) {
		queryString = "%" + queryString + "%";
		Object[] params = { queryString ,classId};
		return sd.getStudentList("select * from student s where s.bu_id||s.last_name||s.first_name like ? and class_id = ?", params);
	}
}
