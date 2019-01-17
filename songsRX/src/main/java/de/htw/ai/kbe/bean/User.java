package de.htw.ai.kbe.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	// Builder weggenommen
	// Attribute nach DB und Beleg 4 angepasst

	public User() {

	}

	@Id
	private String id; // Foreign Key

	private String lastName;
	private String firstName;
	

	public String getId() {
		return id;
	}

	public void setId(String userId) {
		this.id = userId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
		this.id = builder.userID;
		this.lastName = builder.lastName;
		this.firstName = builder.firstName;
	}

}
