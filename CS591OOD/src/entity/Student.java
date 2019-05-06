package entity;

import java.io.Serializable;


/**
 * The persistent class for the student database table.
 * 
 */
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	private int stuId;

	private int classId;

	private String firstName;

	private String lastName;
// 0 for undergrad, 1 for grad. 
	private int stuType;
	
	private String buId;
	

	public Student() {
	}
	

	public Student(int stuId, int classId, String firstName, String lastName, int stuType,String buId) {
		super();
		this.stuId = stuId;
		this.classId = classId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.stuType = stuType;
		this.buId=buId;
	}

	
	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public int getStuId() {
		return this.stuId;
	}

	public void setStuId(int stuId) {
		this.stuId = stuId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getStuType() {
		return this.stuType;
	}

	public void setStuType(int stuType) {
		this.stuType = stuType;
	}
	
	
	public boolean equals(Student obj) {
		if(obj==null)
			return false;
		if(this.getBuId().equals(obj.getBuId()) && this.getLastName().equals(obj.getLastName()) && this.getClassId()==obj.getClassId() && this.getFirstName().equals(obj.getFirstName() )){
			return true;
		}
		else
			return false;
	}


	@Override
	public String toString() {
		return "Student [stuId=" + stuId + ", classId=" + classId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", stuType=" + stuType + ", buId=" + buId + "]";
	}

}