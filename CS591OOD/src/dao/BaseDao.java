package dao;

import java.util.List;

public interface BaseDao {

	public boolean update(String sql, Object[] params);

	public boolean insert(String sql, Object[] params);

	public boolean delete(String sql, Object[] params);

}
