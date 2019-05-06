package service.impl;

import java.util.List;

import dao.ClassesDao;
import dao.impl.ClassesDaoImpl;
import entity.Classes;
import service.ClassesService;

public class ClassesServiceImpl implements ClassesService {

	private ClassesDao cd = new ClassesDaoImpl();
	@Override
	public List<Classes> getClassesList() {
		return cd.getClassesList("select * from classes");
	}

	@Override
	public boolean editClass(Classes c) {
		String sql = "UPDATE classes SET class_name = ? , semester = ? WHERE class_id = ?;";
		Object[] params = {c.getClassName(),c.getSemester(),c.getClassId()};
		return cd.update(sql, params)  ;
	}

	@Override
	public boolean deleteClass(Classes c) {
		String sql = "DELETE FROM classes WHERE class_id = ?;";
		Object[] params = {c.getClassId()};
		return cd.delete(sql, params);
	}

	@Override
	public int addClass(Classes c) {
		String sql="INSERT INTO classes (class_name, semester)  VALUES (?, ?);";
		Object[] params = {c.getClassName(),c.getSemester()};
		return cd.addClassAndGetId(sql, params);
	}

}
