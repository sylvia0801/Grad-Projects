package entity;

import java.io.Serializable;

/**
 * The persistent class for the grade database table.
 * 
 */
public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gradeId;

	private double score;

	private int subTaskId;

	private int stuId;

	public Grade() {
	}

	public Grade(int gradeId, double score, int subTaskId, int stuId) {
		super();
		this.gradeId = gradeId;
		this.score = score;
		this.subTaskId = subTaskId;
		this.stuId = stuId;
	}

	public int getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getSubTaskId() {
		return this.subTaskId;
	}

	public void setSubTaskId(int subTaskId) {
		this.subTaskId = subTaskId;
	}

	
	public int getStuId() {
		return stuId;
	}

	public void setStuId(int stuId) {
		this.stuId = stuId;
	}

	@Override
	public String toString() {
		return "Grade [gradeId=" + gradeId + ", score=" + score + ", subTaskId=" + subTaskId + ", stuId=" + stuId + "]";
	}
	
	

}