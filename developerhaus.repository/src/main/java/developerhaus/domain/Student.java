package developerhaus.domain;

/**
 * 
 * @author jin
 *
 */
public class Student {
	
	private String number;
	private String name;
	private int year;
	private String dept;
	
	public Student(){	
	}
	
	public Student(String number, String name, int year, String dept) {
		super();
		this.number = number;
		this.name = name;
		this.year = year;
		this.dept = dept;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
}
