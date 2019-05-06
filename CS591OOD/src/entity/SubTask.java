package entity;

import java.io.Serializable;


public class SubTask implements Serializable {
	private static final long serialVersionUID = 1L;

	private int subTaskId;

	private String subTaskName;

	private int taskId;

	private double weight;

	public SubTask() {
	}
	
	public SubTask(int subTaskId, String subTaskName, int taskId, double weight) {
		super();
		this.subTaskId = subTaskId;
		this.subTaskName = subTaskName;
		this.taskId = taskId;
		this.weight = weight;
	}

	public int getSubTaskId() {
		return this.subTaskId;
	}

	public void setSubTaskId(int subTaskId) {
		this.subTaskId = subTaskId;
	}

	public String getSubTaskName() {
		return this.subTaskName;
	}

	public void setSubTaskName(String subTaskName) {
		this.subTaskName = subTaskName;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "SubTask [subTaskId=" + subTaskId + ", subTaskName=" + subTaskName + ", taskId=" + taskId + ", weight="
				+ weight + "]";
	}
	
}