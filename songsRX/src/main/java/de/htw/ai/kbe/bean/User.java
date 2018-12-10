package de.htw.ai.kbe.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO - represents a song in the Database
 */
@XmlRootElement(name = "user")
public class User {
	
	private String userID;
	private String lastName;
	private String firstName;
	private Integer id;
	
	public User() {
		
	}
	
	public static class Builder {
		private String userID;
		private String lastName;
		private String firstName;
		private Integer id;
		
		public Builder(String userID) {
			this.userID = userID;
		}
		
		public Builder lastName(String val) {
			lastName = val;
			return this;
		}

		public Builder firstName(String val) {
			firstName = val;
			return this;
		}

		public Builder id(Integer val) {
			id = val;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
	}
	
	private User(Builder builder) {
		this.userID = builder.userID;
		this.lastName = builder.lastName;
		this.firstName = builder.firstName;
		this.id = builder.id;
	}

	@XmlElement
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@XmlElement
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", lastName=" + lastName + ", firstName=" + firstName + ", id=" + id + "]";
	}

}
