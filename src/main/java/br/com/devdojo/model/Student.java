package br.com.devdojo.model;

import javax.persistence.Entity;

@Entity
public class Student extends AbstractEntity {
	
	private static final long serialVersionUID = 1533487325734563596L;
	
	private String name;
	
	public Student() {
	}

	public Student(String name) {
		this.name = name;
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
