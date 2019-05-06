package entity.dto;

import entity.Student;

import java.util.List;

import entity.Grade;

public class StudentGradingDto extends Student {
	private List<Grade> grades;
	
	public StudentGradingDto(int stuId, int classId, String firstName, String lastName, int stuType, String buId,
			List<Grade> grades) {
		super(stuId, classId, firstName, lastName, stuType, buId);
		this.grades = grades;
	}
	public StudentGradingDto(Student s,List<Grade> grades) {
		super(s.getStuId(), s.getClassId(), s.getFirstName(), s.getLastName(), s.getStuType(), s.getBuId());
		this.grades = grades;
	}

	
	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}
	@Override
	public String toString() {
		String grading="";
		for(Grade g:grades) {
			grading = grading+g.toString();
		}
		return "Student [stuId=" + this.getStuId() + ", classId=" + this.getClassId() + ", firstName=" + this.getFirstName() + ", lastName="
				+ getLastName() + ", stuType=" + getStuType() + ", buId=" + getBuId() +"grades=" + grading + "]";
	}
	
	
}
