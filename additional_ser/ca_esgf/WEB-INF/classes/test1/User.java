package test1;

import java.io.Serializable;
import java.lang.Character;
import java.lang.Integer;
import javax.persistence.*;
import java.lang.String;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="public.user")
@NamedQueries(
 {
   @NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
   @NamedQuery(name="User.findUsername", query="SELECT u.username FROM User u"),
 }
)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String city;

	private String country;

	private String dn;

	private String email;

	private String firstname;
        @Id
	private Integer id;

	private String lastname;

	private String middlename;

	@Column(name="notification_code")
	private Integer notificationCode;

	private String openid;

	private String organization;

	@Column(name="organization_type")
	private String organizationType;

	private String password;

	private String state;

	@Column(name="status_code")
	private Integer statusCode;

	private String username;

	@Column(name="verification_token")
	private String verificationToken;

	public User() {
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDn() {
		return this.dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public Integer getNotificationCode() {
		return this.notificationCode;
	}

	public void setNotificationCode(Integer notificationCode) {
		this.notificationCode = notificationCode;
	}

	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationType() {
		return this.organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVerificationToken() {
		return this.verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

}
