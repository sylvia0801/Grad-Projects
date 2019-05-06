package dao.impl;

import java.util.List;

import dao.BaseDao;
import tools.DBUtil;

public class BaseDaoImpl implements BaseDao {

	@Override
	public boolean update(String sql, Object[] params) {
		return DBUtil.update(sql, params);
	}

	@Override
	public boolean insert(String sql, Object[] params) {
		return DBUtil.update(sql, params);
	}

	@Override
	public boolean delete(String sql, Object[] params) {
		return DBUtil.update(sql, params);
	}
}
