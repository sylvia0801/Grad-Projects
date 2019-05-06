package service;

import java.util.List;

import entity.Grade;
import entity.dto.GradeDto;
import entity.dto.StudentGradingDto;

public interface GradeService {
	//student lists with grade
	public List<StudentGradingDto> getGradingList(int classId);
	//grades of one student
	public List<GradeDto> getGrades(int stuId);
	public boolean giveGrade(int stuId,int subTaskID,double score);
	public boolean changeGrade(Grade g);
}
