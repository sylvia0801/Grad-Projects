package service;

import java.util.List;

import entity.Classes;
import entity.Student;

public interface StudentService {
	public List<Student> getStudentList(int classId);
	public Student getStudent(int stuId);
	public boolean editStudent(Student s);
	public boolean deleteStudent(Student s);
	public boolean addStudent(Student s);
	public boolean LoadStudentFromCsv(String filepath,int classId);
	public List<Student> searchStudent(String queryString,int classId);
}
