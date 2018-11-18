package com.JCG.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Entity
@Table(name = "user")
public class Users {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")

	private Integer id;
	@Column(name = "name")

	private String name;

	private Integer salary;

	private String teamName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@JacksonXmlElementWrapper(localName="userslogs")
	@JacksonXmlProperty(localName="userlog")
	private List<UsersLog> userslog;
	

	public List<UsersLog> getUsersLogs() {
		return userslog;
	}

	public void setUsersLogs(List<UsersLog> usersLogs) {
		this.userslog = usersLogs;
	}

	public Users() {
	}

	public Integer getId() {
		return id;
	}

	public Users setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Users setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getSalary() {
		return salary;
	}

	public Users setSalary(Integer salary) {
		this.salary = salary;
		return this;
	}

	public String getTeamName() {
		return teamName;
	}

	public Users setTeamName(String teamName) {
		this.teamName = teamName;
		return this;
	}
}
