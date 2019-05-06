package entity;

import java.io.Serializable;


/**
 * The persistent class for the classes database table.
 * 
 */
public class Classes implements Serializable {
	private static final long serialVersionUID = 1L;

	private int classId;

	private String className;

	private String semester;

	public Classes() {
	}
	
	public Classes(int classId, String className, String semester) {
		super();
		this.classId = classId;
		this.className = className;
		this.semester = semester;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSemester() {
		return this.semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	@Override
	public String toString() {
		return "Classes [classId=" + classId + ", className=" + className + ", semester=" + semester + "]";
	}
	
}