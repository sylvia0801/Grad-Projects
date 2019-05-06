package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dao.ClassesDao;
import entity.Classes;
import service.ClassesService;
import tools.DBUtil;

public class ClassesDaoImpl extends BaseDaoImpl implements ClassesDao {
	
	@Override
	public List<Classes> getClassesList(String sql) {
		List<Classes> data = new ArrayList<Classes>();
		try {
			CachedRowSet crs = DBUtil.select(sql);
			while(crs.next()) {
				data.add(new Classes(Integer.parseInt(crs.getString(1)), crs.getString(2), crs.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public int addClassAndGetId(String sql, Object[] params) {
		return DBUtil.insertAndGetId(sql, params);
	}

}
