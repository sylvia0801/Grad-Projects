package entity.dto;

import java.util.List;

import entity.Task;
import entity.SubTask;

public class TaskDto extends Task {
	private List<SubTask> subTaskList;
	
	public TaskDto(int taskId, int classId, String taskName, double weight, List<SubTask> subTaskList) {
		super(taskId, classId, taskName, weight);
		this.subTaskList = subTaskList;
	}

	public List<SubTask> getSubTaskList() {
		return subTaskList;
	}

	public void setSubTaskList(List<SubTask> subTaskList) {
		this.subTaskList = subTaskList;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + getTaskId() + ", classId=" + getClassId() + ", taskName=" + getTaskName() + ", weight=" + getWeight()
				+ " TaskDto [subTaskList=" + subTaskList + "]";
	}
	
	
}
