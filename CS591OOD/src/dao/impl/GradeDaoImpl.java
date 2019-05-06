package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dao.GradeDao;
import entity.Grade;
import entity.Student;
import entity.dto.GradeDto;
import tools.DBUtil;

public class GradeDaoImpl extends BaseDaoImpl implements GradeDao {

	@Override
	public List<GradeDto> getGradeOfStudent(String sql, int stuId) {

		List<GradeDto> gl = new ArrayList<GradeDto>();
		Object[] params = { stuId };
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while (crs.next()) {
				gl.add(new GradeDto(Integer.parseInt(crs.getString(1)), Double.parseDouble(crs.getString(2)),
						Integer.parseInt(crs.getString(3)), Double.parseDouble(crs.getString(4)), stuId,
						crs.getString(6), crs.getString(7)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gl;
	}

	@Override
	public List<Grade> getGrade(String sql, int stuId) {
		List<Grade> gl = new ArrayList<>();
		Object[] params = { stuId };
		try {
			CachedRowSet crs = DBUtil.select(sql, params);
			while (crs.next()) {
				gl.add(new Grade(Integer.parseInt(crs.getString(1)), Double.parseDouble(crs.getString(3)),
						Integer.parseInt(crs.getString(2)), stuId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gl;
	}

}
