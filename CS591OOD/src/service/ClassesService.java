package service;

import java.util.List;

import entity.Classes;

public interface ClassesService  {
	public List<Classes> getClassesList();
	public boolean editClass(Classes c);
	public boolean deleteClass(Classes c);
	public int addClass(Classes c);
}
