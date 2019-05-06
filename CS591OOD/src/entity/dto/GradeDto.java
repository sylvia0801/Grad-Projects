package entity.dto;

import entity.Grade;

public class GradeDto extends Grade {
	private String taskName;
	private String subTaskName;
	private double weight;
	public GradeDto(int gradeId, double score, int subTaskId,double weight, int stuId, String taskName, String subTaskName) {
		super(gradeId, score, subTaskId, stuId);
		this.taskName = taskName;
		this.subTaskName = subTaskName;
		this.weight=weight;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getSubTaskName() {
		return subTaskName;
	}
	public void setSubTaskName(String subTaskName) {
		this.subTaskName = subTaskName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
