package de.htw.ai.kbe.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="users")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	
	//Builder weggenommen
	//Attribute nach DB und Beleg 4 angepasst
	
	public User() {
		
	}

    @Id
    private String id; //Foreign Key
    
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
}
