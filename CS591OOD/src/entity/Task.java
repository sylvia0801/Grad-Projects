package entity;

import java.io.Serializable;


public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	private int taskId;

	private int classId;

	private String taskName;

	private double weight;

	public Task() {
	}
	
	public Task(int taskId, int classId, String taskName, double weight) {
		super();
		this.taskId = taskId;
		this.classId = classId;
		this.taskName = taskName;
		this.weight = weight;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", classId=" + classId + ", taskName=" + taskName + ", weight=" + weight
				+ "]";
	}
	

}