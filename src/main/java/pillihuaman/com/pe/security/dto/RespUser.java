package pillihuaman.com.pe.security.dto;


import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor // Add this annotation for the all-argument constructor
@Builder
@ToString
public class RespUser {

	private ObjectId id;
	private String alias;
	private  ObjectId idSystem;
	private String mail;
	private String mobilPhone;
	private String user;
	private String userName;
	private   boolean enabled;
	private ObjectId idRol;
	private String access_token;
	private String tenantId;
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getIdSystem() {
		return idSystem;
	}

	public void setIdSystem(ObjectId idSystem) {
		this.idSystem = idSystem;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobilPhone() {
		return mobilPhone;
	}

	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ObjectId getIdRol() {
		return idRol;
	}

	public void setIdRol(ObjectId idRol) {
		this.idRol = idRol;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}


