package test1;

import java.io.Serializable;
import java.lang.Character;
import java.lang.Integer;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: postgres_user_table_pers
 *
 */


@Entity
@Table(name="user")

public class User implements Serializable {

	
	private Integer id;
	private Character firstname;
	private Character middlename;
	private Character lastname;
	private Character email;
	private Character username;
	private Character password;
	private Character dn;
	private Character openid;
	private Character city;
	private Character state;
	private Character country;
	private Integer status_code;
	private Character verification_token;
	private Integer notification_code;
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public Character getFirstname() {
		return this.firstname;
	}

	public void setFirstname(Character firstname) {
		this.firstname = firstname;
	}   
	public Character getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(Character middlename) {
		this.middlename = middlename;
	}   
	public Character getLastname() {
		return this.lastname;
	}

	public void setLastname(Character lastname) {
		this.lastname = lastname;
	}   
	public Character getEmail() {
		return this.email;
	}

	public void setEmail(Character email) {
		this.email = email;
	}   
	public Character getUsername() {
		return this.username;
	}

	public void setUsername(Character username) {
		this.username = username;
	}   
	public Character getPassword() {
		return this.password;
	}

	public void setPassword(Character password) {
		this.password = password;
	}   
	public Character getDn() {
		return this.dn;
	}

	public void setDn(Character dn) {
		this.dn = dn;
	}   
	public Character getOpenid() {
		return this.openid;
	}

	public void setOpenid(Character openid) {
		this.openid = openid;
	}   
	public Character getCity() {
		return this.city;
	}

	public void setCity(Character city) {
		this.city = city;
	}   
	public Character getState() {
		return this.state;
	}

	public void setState(Character state) {
		this.state = state;
	}   
	public Character getCountry() {
		return this.country;
	}

	public void setCountry(Character country) {
		this.country = country;
	}   
	public Integer getStatus_code() {
		return this.status_code;
	}

	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}   
	public Character getVerification_token() {
		return this.verification_token;
	}

	public void setVerification_token(Character verification_token) {
		this.verification_token = verification_token;
	}   
	public Integer getNotification_code() {
		return this.notification_code;
	}

	public void setNotification_code(Integer notification_code) {
		this.notification_code = notification_code;
	}
   
}
