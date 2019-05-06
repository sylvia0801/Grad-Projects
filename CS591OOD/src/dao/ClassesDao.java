package dao;

import java.util.List;

import entity.Classes;

public interface ClassesDao  extends BaseDao{
	public List<Classes> getClassesList(String sql);
	public int addClassAndGetId(String sql,Object[] params);
}
