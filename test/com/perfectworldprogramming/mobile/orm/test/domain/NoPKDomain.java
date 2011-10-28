package com.perfectworldprogramming.mobile.orm.test.domain;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ColumnType;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;

public class NoPKDomain {

	@Column(value="value1", type=ColumnType.TEXT)
	private String value1;
	
	@Column(value="value1", type=ColumnType.TEXT)
	private String value2;
	
	@ForeignKey(value="PERSON_ID")
	private Person person;

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	
}
