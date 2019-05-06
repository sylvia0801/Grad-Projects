package dao;

import java.util.List;

import entity.Grade;
import entity.dto.GradeDto;
import entity.dto.StudentGradingDto;

public interface GradeDao extends BaseDao {
	public List<GradeDto> getGradeOfStudent(String sql,int stuId);
	public List<Grade> getGrade(String sql,int stuId);
}
